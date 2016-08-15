package com.kk.lp.deviceInfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.kk.lp.R;
import com.kk.lp.databinding.ActivityDeviceInfoBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

public class DeviceInfoActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1000;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1001;
    ActivityDeviceInfoBinding binding;
    private String device_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_info);
        binding.keyGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceInfo();
            }
        });

        binding.uuidGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uuid_show.setText(new UUID("abc".hashCode(), "bcd".hashCode()).toString());
                binding.uuidShow.setText(UUID.randomUUID().toString());

            }
        });
        binding.imeiGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission(MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        });
        binding.settingGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //23开始无法写入到settings中,会抛出一个异常java.lang.IllegalArgumentException: You cannot keep your settings in the secure settings.
                if (checkSystemWritePermission()) {
//                    getPermission(MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    writeSettings();
                }
            }
        });
        binding.writeGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeStorage();
//                getPermission(MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient
                            .getAdvertisingIdInfo(DeviceInfoActivity.this);
                    String advertisingId = adInfo.getId();
                    boolean optOutEnabled = adInfo.isLimitAdTrackingEnabled();
                    System.out.println("adi=====" + advertisingId);
                    System.out.println("adi_opt=====" + optOutEnabled);
                    // Log.i("ABC", "advertisingId" + advertisingId);
                    // Log.i("ABC", "optOutEnabled" + optOutEnabled);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            if (retVal) {
                Toast.makeText(this, "Write allowed :-)", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Write not allowed :-(", Toast.LENGTH_LONG).show();
                openAndroidPermissionsMenu();
            }
        }
        return retVal;
    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + DeviceInfoActivity.this.getPackageName()));
        startActivity(intent);
    }

    private void writeSettings() {
        Settings.System.putString(getContentResolver(), "custom_setting", "123456789abc");
        binding.settingShow.setText(Settings.System.getString(getContentResolver(), "custom_setting"));
    }


    private void writeStorage() {
        if (DeviceInfoActivity.this.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            writeToFile();
            binding.writeShow.setText(readFromFile());
        }
    }

    private void writeToFile() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                /* 创建一个读写锁 */
                ReadWriteLock rwlock = new ReentrantReadWriteLock();
                rwlock.writeLock().lock();
                try {
                    File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LMDevice");
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    File file = new File(fileDir, "lm_device_id");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write("你好".getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }finally {
                    rwlock.writeLock().unlock();
                }
            }
        }).start();

    }

    private String readFromFile() {
         /* 创建一个读写锁 */
        ReadWriteLock rwlock = new ReentrantReadWriteLock();
        rwlock.readLock().lock();
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LMDevice");
        File file = new File(fileDir, "lm_device_id");
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String s;
            while ((s = in.readLine()) != null){
                sb.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            rwlock.readLock().unlock();
        }
        return sb.toString();
    }
    /*
* Generate a new EC key pair entry in the Android Keystore by
* using the KeyPairGenerator API. The private key can only be
* used for signing or verification and only with SHA-256 or
* SHA-512 as the message digest.
*/

    private void getDeviceInfo() {
        device_info = "";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            device_info += getIMEI();
        }
        String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        device_info += "ANDROID_ID=====" + ANDROID_ID + "\n";
        String SerialNumber = android.os.Build.SERIAL;
        device_info += "SerialNumber=====" + SerialNumber + "\n";
        device_info += "mac地址=====" + getMac() + "\n";
        device_info += "cpu型号=====" + getCpuInfo() + "\n";
        device_info += "Psuedoid_param=====Build.BOARD=" + Build.BOARD + ",Build.CPU_ABI="
                + Build.CPU_ABI + ",Build.BRAND=" + Build.BRAND + ",Build.DEVICE=" + Build.DEVICE
                + ",Build.MANUFACTURER=" + Build.MANUFACTURER + ",Build.MODEL=" + Build.MODEL + ",Build.PRODUCT=" + Build.PRODUCT + ",Build.FINGERPRINT="+ Build.FINGERPRINT + "\n";
        device_info += "Psuedoid=====" + getUniquePsuedoID() + "\n";
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        device_info += "serial=====" + serial + "\n";

        binding.keyShow.setText(device_info);
        
    }

    //获得IMEI号
    private String getIMEI() {
        String imei = "";
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //通信模块才会有IMEI号,两个通信模块就会有两个IMEI号
        imei = "DEVICE_ID(IMEI)=====" + tm.getDeviceId() + "\n";
        //sim卡的设备
        imei += "SimSerialNumber=====" + tm.getSimSerialNumber() + "\n";
        return imei;
    }

    String getMac() {
        String macSerial = null;
        String str = "";
        String streth = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
            Process ppeth = Runtime.getRuntime().exec(
                    "cat /sys/class/net/eth0/address");
            InputStreamReader ireth = new InputStreamReader(ppeth.getInputStream());
            LineNumberReader inputeth = new LineNumberReader(ireth);

            for (; null != streth; ) {
                streth = inputeth.readLine();
                if (streth != null) {
                    macSerial += ";" + streth.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    String getCpuInfo() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * Return pseudo unique ID
     *
     * @return ID
     */
    public String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BRAND.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serialNumber = "";
        String androidId;
        String deviceInfo;
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
                serialNumber = Build.SERIAL;
            }

            // Go ahead and return the serial for api => 9
            androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceInfo = serialNumber + "," + androidId;
            return new UUID(m_szDevIDShort.hashCode(), deviceInfo.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            deviceInfo = "deviceInfo"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), deviceInfo.hashCode()).toString();
    }

    public void getPermission(final int permission) {
        String sysPermission = Manifest.permission.READ_PHONE_STATE;
        switch (permission) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                sysPermission = Manifest.permission.READ_PHONE_STATE;
                break;
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                sysPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
            default:
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    sysPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                //没有权限

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        sysPermission)) {


                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    final String finalSysPermission = sysPermission;
                    Snackbar.make(binding.uuidGet, "请授予权限",
                            Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(DeviceInfoActivity.this,
                                    new String[]{finalSysPermission},
                                    permission);
                        }
                    })
                            .show();
                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{sysPermission},
                            permission);
                }
            } else {
                switch (permission) {
                    case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                        binding.imeiShow.setText(getIMEI());
                        break;
                    case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                        writeStorage();
                        break;
                    default:
                        break;
                }
            }
        } else {
            switch (permission) {
                case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                    binding.imeiShow.setText(getIMEI());
                    break;
                case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                    writeStorage();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授予电话的权限", Toast.LENGTH_LONG).show();
                    binding.imeiShow.setText(getIMEI());
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "未授予电话的权限", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授予读取文件的权限", Toast.LENGTH_LONG).show();
                    writeStorage();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "未授予读取文件的权限", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //同时存在的意思代表一条数据同时满足两个条件

    /**
     * <p>生成的唯一标识组合分类:</p>
     * <li>根据IMEI号及androidId生成</li>
     * <li>根据IMEI号及SN号生成</li>
     * <li>根据IMEI号及device_info生成</li>
     * <li>根据androidId及SN号生成</li>
     * <li>根据androidId及device_info生成</li>
     * <li>根据SN号及device_info生成</li>
     * <li>无任何有效标识,随机生成</li>
     * <li></li>
     *
     * @return
     */
    public String device_id() {
        String android_id = "";//无效数据:9774d56d682e549c,null
        String serial_number = "";//无效数据:unknown,
        String imei = "";//无效数据:000000000000000,含有*
        //设备信息生成的一个串
        String device_info = "7439810348013840";
        int new_os_version = 23;
        int old_os_version = 21;

        if (serial_number.equals("unknown")) {
            //模拟器取得的结果是unknown,将其置为空字符串
            serial_number = "";
        }
        //Android 6.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //AndroidID及SN号逻辑
            if (validAndroidID(android_id)) {
                //有效的AndroidID
                if (!serial_number.equals("")) {
                    //SN号不为空
                    if (existAndroidId(android_id) && existSerialNumber(serial_number)) {
                        //如果同时满足AndroidID跟serialNumber相同,则返回查询到device_id
                        return getDeviceId(android_id, serial_number);
                    } else {
                        //有一条不满足则重新生成一个device_id
                        return createDeviceId(android_id, serial_number);
                    }
                } else {
                    //SN号为空字符串,androidID不为空,那就根据device_info生成唯一标识
                    if (existAndroidId(android_id) && existDeviceInfo(device_info)) {
                        //如果同时满足AndroidID跟serialNumber相同,则返回查询到device_id
                        return getDeviceIdByAid(android_id, device_info);
                    } else {
                        //有一条不满足则重新生成一个device_id
                        return createDeviceIdByAid(android_id, device_info);
                    }
                }
            } else {
                //无效的AndroidID,没有办法了,只能根据SN号及设备信息创建一个device_id
                return createDeviceIdBySN(serial_number, device_info);
            }
        } else {
            //Android 6.0以下
            if (validIMEI(imei)) {
                //imei有效逻辑
                if (validAndroidID(android_id)) {
                    //imei号有效,androidid有效
                    if (countByIMEI(imei) == 0) {
                        //没有存在的设备
                        return createDeviceIdByIMEI(imei, android_id);
                    } else {
                        //多个相同IMEI号的设备,IMEI号可能有重复现象,进行处理
                        if (existIMEI(imei) && existAndroidId(android_id)) {
                            //数据库中已有IMEI号及对应的AndroidID,认为为一台设备
                            return getDeviceIdByIMEI(imei, android_id);
                        } else {
                            //以上两个条件不能同时满足,则认为为一台新的设备
                            return createDeviceIdByIMEI(imei, android_id);
                        }
                    }
                } else {
                    //Androidid无效,但是IMEI号有效
                    if (countByIMEI(imei) == 0) {
                        //新的设备
                        if (!serial_number.equals("")) {
                            //SN号有效
                            return createDeviceIdByIMEIAndSN(imei, serial_number);
                        } else {
                            //SN号无效,根据device_info生成
                            return createDeviceIdByIMEIAndDeviceInfo(imei, device_info);
                        }
                    } else {
                        //已有相同IMEI号设备
                        if (!serial_number.equals("")) {
                            if (existIMEI(imei) && existSerialNumber(serial_number)) {
                                //数据库中已有IMEI号及对应的SN号,认为为一台设备
                                return getDeviceIdByIMEIAndSN(imei, serial_number);
                            } else {
                                //以上两个条件不能同时满足,则认为为一台新的设备
                                return createDeviceIdByIMEIAndSN(imei, serial_number);
                            }
                        } else {
                            //SN号为空字符串,androidID也无效,IMEI号有效,那就根据device_info生成唯一标识
                            if (existAndroidId(android_id) && existDeviceInfo(device_info)) {
                                //如果同时满足IMEI号跟device_info相同,则返回查询到device_id
                                return getDeviceIdByIMEIAndDeviceInfo(imei, device_info);
                            } else {
                                //有一条不满足则重新生成一个device_id
                                return createDeviceIdByIMEIAndDeviceInfo(imei, device_info);
                            }
                        }
                    }
                }

            } else {
                //imei无效,走上面的AndroidID 及 SN号逻辑
            }
        }
        return "";
    }

    public boolean existAndroidId(String android_id) {
        return false;
    }

    public boolean existSerialNumber(String serial_number) {
        return false;
    }

    public boolean existIMEI(String imei) {
        return false;
    }

    public boolean existDeviceInfo(String device_info) {
        return false;
    }

    public String getDeviceId(String android_id, String serial_number) {
        return "通过Android_id及SN号查询到的device_id";
    }

    public String getDeviceIdByIMEIAndDeviceInfo(String imei, String device_info) {
        return "通过IMEI号及device_info查询到的device_id";
    }

    public String getDeviceIdByIMEI(String imei) {
        return "通过IMEI号查询到的device_id";
    }

    public String getDeviceIdByIMEI(String imei, String android_id) {
        return "通过IMEI号及android_id查询到的device_id";
    }

    public String getDeviceIdByIMEIAndSN(String imei, String android_id) {
        return "通过IMEI号及SN号查询到的device_id";
    }

    public String createDeviceId(String android_id, String serial_number) {
        return "根据AndroidID及SN号生成的device_id";
    }

    public String createDeviceIdByIMEI(String imei, String android_id) {
        return "根据IMEI号及android ID生成的device_id";
    }

    public String createDeviceIdByIMEIAndSN(String imei, String serialNumber) {
        return "根据IMEI号及SN号生成的device_id";
    }

    public String createDeviceIdByIMEIAndDeviceInfo(String imei, String device_info) {
        return "根据IMEI号及device_info生成的device_id";
    }

    public String createDeviceIdBySN(String serialNumber, String device_info) {
        if (!serialNumber.equals("")) {
            return "根据SN号及device_info生成device_id";
        } else {
            return "随机数吧,什么都没有,怎么生成,应该不会发生这种情况!!!";
        }
    }

    public String createDeviceIdByAid(String android_id, String device_info) {
        return "根据android_id及device_info生成的device_id";
    }

    public String getDeviceIdByAid(String android_id, String device_info) {
        return "根据android_id及device_info返回device_id";
    }


    public int countByIMEI(String imei) {
        int count = 1;
        //返回具有相同IMEI号的数量
        return count;
    }

    public boolean validIMEI(String imei) {
        String regex = "\\d{15}|\\d{17}";
        //取值范围为15位或17位的纯数字，正则表达式：“\d{15}|\d{17}”
        if (Pattern.matches(regex, imei) && !imei.equals("0000000000000000") && !imei.equals("000000000000000000")) {
            return true;
        }
        return false;
    }

    public boolean validAndroidID(String android_id) {
        String regex = "[0-9a-f]{15,16}";
        //取值为64位的整数，以十六进制表示，正则表达式为：“[0-9a-f]{16}”,我的华为手机是15位,因此正则表达式最好是15-16位
        if (Pattern.matches(regex, android_id) && !android_id.equals("000000000000000") && !android_id.equals("0000000000000000") && !android_id.equals("9774d56d682e549c")) {
            return true;
        }
        return false;
    }


}
