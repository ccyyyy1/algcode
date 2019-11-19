package com.ex.cy.demo4;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.PathClassLoader;


public class MainActivity extends Activity {

    MemoryFile mf;

    // Used to load the 'native-lib' library on application startup.
    static {//<cinit>
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ashmem();

        try {
            fix(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Example of a call to a native method
        final TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("bbbbbbbbbb");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = fun1();
                int b = fun2();
                int c = a + b;//4+5 = 9    , 4+4=8
                c += MC1.fun3();
                tv.setText(stringFromJNI() + " C:" + c);
//                try {
//                    Process p = Runtime.getRuntime().exec(new String[]{"getprop"});
//                    int res = p.waitFor();
//                    Toast.makeText(MainActivity.this, "res : " + res, Toast.LENGTH_LONG).show();
//                    String sb = getStringBuffer(p.getInputStream());
//                    Log.i("TTTT", "os: " + sb.toString());
//                    String sb2 = getStringBuffer(p.getErrorStream());
//                    Log.i("TTTT", "es: " + sb2.toString());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    private void ashmem() {
        try {
            MemoryFile mf = new MemoryFile("TTT", 1024);
//            mf.writeBytes();
//            mf.readBytes()
            Field fileDescriptorField = MemoryFile.class.getDeclaredField("mFD");
            fileDescriptorField.setAccessible(true);
            FileDescriptor mFD = (FileDescriptor) fileDescriptorField.get(mf);
            ParcelFileDescriptor dupmfd = ParcelFileDescriptor.dup(mFD);
            //  cat maps | grep ashmem
            //  7fb24ed000-7fb24ee000 rw-s 00000000 00:01 1633355                        /dev/ashmem/TTT (deleted)
            //C
            // ashmem_create_region()
            //1. open , set name
            //  __ashmem_open()
            //      open(ASHMEM_DEVICE, O_RDWR)    //1.open  /dev/ashmem    get fd
            //          |内核层
            //                zalloc一个ashmem_area 结构， asma->size = length
            //  ioctl(fd, ASHMEM_SET_NAME, name)   //2.ioctl ASHMEM_SET_NAME  set name
            //2. mmap
            //  mmap(NULL, length, prot = PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0)
            //      |内核层
            //          shmem_file_setup()  前缀ASHMEM_NAME_PREFIX+名字  是原生linux的共享内存机制
            //              d_alloc(root, &this);//分配dentry cat/proc/pid/maps可以查到     ,shmem_acct_size() , shmem_get_inode()   //分配inode , d_instantiate(dentry, inode);//绑定 , alloc_file()
            //              通过shmem_file_setup在tmpfs临时文件系统中创建一个临时文件（也许只是内核中的一个inode节点）
            //          shmem_set_file()    //在这里把vma->vm_file =  asma->file，  也就是把对/dev/ashmem open后的文件在内核中重新指向了刚才创建的内存临时文件，以后调用mmap其实是对这个内存文件进行mmap而不是/dev/ashmem设备文件

            //todo give to other process dupmfd with binder(ashmem IPC)
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
//fixDex  MC1.fun3()
    public static void fix(Context c) throws Exception {
        //1.解包 dex
        ApplicationInfo ai = c.getApplicationInfo();
        String apkPath = ai.sourceDir;
        ZipFile zipFile = new ZipFile(apkPath);
        ZipEntry dexZE = zipFile.getEntry("classes2.dex");
        InputStream is = zipFile.getInputStream(dexZE);

        //2.读dex到内存
        ByteArrayOutputStream boo = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer, 0, buffer.length)) >= 0) {
            boo.write(buffer, 0, len);
        }
        byte[] all = boo.toByteArray();
        String hex = strTo16(all);
        System.out.println(hex);

        //3.dex修复
//        String b1 = new String()
        byte[][] bsPatch = new byte[][]{
                {-0x5B,0x2F,-0x1B,-0x2B,-0x35,-0x21,0x28,0x3D,0x5C,0x4F,0x16,0x58,0x17,-0x5D,0x0C,0x1C,0x0D,-0x58,0x60,0x09,-0x36,0x70,0x15,-0x76},
                {0x01,0x09,-0x80,0x02}}; //127 0 255

        ByteBuffer.wrap(all, 8, bsPatch[0].length).put(bsPatch[0]);
        ByteBuffer.wrap(all, 0x15e, bsPatch[1].length).put(bsPatch[1]);

        //4.写dex到cache dir (不好；换成直接让classloader加载内存中的文件)
        File patchedDex = new File(c.getCacheDir(), "" + UUID.randomUUID().toString() + ".dex");
        if (patchedDex.exists())
            patchedDex.delete();
        FileOutputStream fileOutputStream = new FileOutputStream(patchedDex);
        fileOutputStream.write(all);
        fileOutputStream.close();

        //5.加载dex
        //1. 修改父加载器    X
        //2. 热修复类似方案  0
        //3. MultiDex.install 原理?
//        PathClassLoader pc = (PathClassLoader) c.getClassLoader();
//        BaseDexClassLoader bdc = pc;
//        Field pathListF = pc.getClass().getSuperclass().getDeclaredField("pathList");
//        pathListF.setAccessible(true);
//        Object pathList = pathListF.get(bdc);
//        Field dexElementsF = pathListF.getClass().getDeclaredField("dexElements");
//        dexElementsF.setAccessible(true);
//        Object dexElements = dexElementsF.get(pathList);


        PathClassLoader pc = (PathClassLoader) c.getClassLoader();
        BaseDexClassLoader bdc = pc;
        Field pathListF = pc.getClass().getSuperclass().getDeclaredField("pathList");
        pathListF.setAccessible(true);
        Object dexpathlist = pathListF.get(pc);
        Field dexElementsF = dexpathlist.getClass().getDeclaredField("dexElements");
        dexElementsF.setAccessible(true);
        Object dexEs = dexElementsF.get(dexpathlist);
        Object ndexEs = Array.newInstance(Array.get(dexEs, 0).getClass(), Array.getLength(dexEs) + 1);
        //TODO FOR
        Array.set(ndexEs, 0, Array.get(dexEs, 0));

        Method makeDexElementsM = null;
        Method[] ms = dexpathlist.getClass().getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals("makeDexElements")) {
                makeDexElementsM = m;
                break;
            }
        }
        makeDexElementsM.setAccessible(true);
        List<File> fs = new ArrayList<>();
        fs.add(patchedDex);
        List<IOException> ioExceptions = new ArrayList<>();
        Object nE = makeDexElementsM.invoke(dexpathlist.getClass(), fs, null, ioExceptions, pc.getParent());
        Array.set(ndexEs, 1, Array.get(nE, 0));
        dexElementsF.set(dexpathlist, ndexEs);

//        DexClassLoader dcl = new DexClassLoader(patchedDex.getAbsolutePath(), c.getCacheDir().getAbsolutePath(), "", c.getClassLoader());
//        c.getClassLoader()
//        dcl.loadClass("com.ex.cyy.demo4.MC1");
    }

    public static String strTo16(byte[] b) {
        StringBuffer sb = new StringBuffer();
        int ch;
        for (int i = 0; i < b.length; i++) {
            ch = b[i];
            String s4 = Integer.toHexString(ch);
            sb.append(s4);
            if (i > 1) {
                if (i % 2 == 0)
                    sb.append(" ");
                if (i % 32 == 0)
                    sb.append("\n");
            }
        }
        return sb.toString();
    }

    public int fun1() {
        return 4;
    }

    public int fun2() {
        return 5;
    }

    private String getStringBuffer(InputStream is) throws IOException {
//        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[1024 * 8];
        int len = 0;
        ByteArrayOutputStream boo = new ByteArrayOutputStream();
        while ((len = is.read(b, 0, b.length)) != -1) {
            boo.write(b, 0, len);
        }
        byte[] b2 = boo.toByteArray();
        return new String(b2, 0, b2.length);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
