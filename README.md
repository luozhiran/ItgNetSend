# ItgNetSend

#### ItgNetSend配置
```
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ItgNetSend.itg()
        .itgSet()//获取网络配置工具
        .app(this)//保存全局context
        .log("")//设置输入日志路径
        .url("http://www.baidu.com");//输入全局地址，设置此项后，在使用网络请求时，没有输入地址，则              使用该地址请求
    }
}

```
#### ItgNetSend GET请求
> 1.使用全局地址请求（在App中通过ItgNetSend.itg().url("http://www.baidu.com")设置）
```
      ItgNetSend.itg()
                .builder(ItgNetSend.GET)//使用app里设置的地址请求
                .send(new ItgCallback() {
                    @Override
                    public void onFailure(String er) {
                        Log.e("dd", er);
                    }

                    @Override
                    public void onResponse(String result, int code) {
                        Log.e("dd", result);
                    }

                });

```
> 使用当前地址请求("https://github.com/luozhiran/ItgNetSend/new/master?readme=1")
```

       ItgNetSend.itg()
                .builder(ItgNetSend.GET)//使用app里设置的地址请求
                .url("https://github.com/luozhiran/ItgNetSend/new/master?readme=1")
                .send(new ItgCallback() {
                    @Override
                    public void onFailure(String er) {
                        Log.e("dd", er);
                    }

                    @Override
                    public void onResponse(String result, int code) {
                        Log.e("dd", result);
                    }

                });


```

#### ItgNetSend 上传字符串
>　1.上传一个字符串（addContent("","")）
```
    ItgNetSend
             .itg()
             .builder(ItgNetSend.POST)
             .url("http://test.yuanqutech.com:8080/test")
             .addContent("fasdfasdfsadfasdfasdfasdfasdfsda", "application/json; charset=utf-8")
             .send(new ItgCallback() {
                  @Override
                  public void onFailure(String er) {
                   ItgLog.e(er);
                  }

                  @Override
                  public void onResponse(String result, int code) {
                        ItgLog.e(result);
                  }
              });

    上传结果:
    {t:"test",off:1790342,p:0,msgid:73619198221683204,msgtime:"2019/06/27　10:56:23.755",serverid:1,size:32,r:0,d:0}�fasdfasdfsadfasdfasdfasdfasdfsda

```
>2.批量上传字符串（addContent("","")）

```
             ItgNetSend
                    .itg()
                    .builder(ItgNetSend.POST)
                    .url("http://test.yuanqutech.com:8080/test")
                    .addContent("fasdfasdfsadfasdfasdfasdfasdfsda", ItgNetSend.MEDIA_JSON)
                    .addContent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",ItgNetSend.MEDIA_JSON)
                    .send(new ItgCallback() {
                        @Override
                        public void onFailure(String er) {
                            ItgLog.e(er);
                        }

                        @Override
                        public void onResponse(String result, int code) {
                            ItgLog.e(result);
                        }
                    });

                上传结果:
                {t:"test",off:1790627,p:0,msgid:73619198831930694,msgtime:"2019/06/27　
                11:06:34.002",serverid:1,size:410,r:0,d:0}
                �--11961578-18bf-4ce7-bce1-3378d1dc7103
                  Content-Disposition: form-data; name=""
                  Content-Type: application/json; charset=utf-8
                   Content-Length: 32

                  fasdfasdfsadfasdfasdfasdfasdfsda
                --11961578-18bf-4ce7-bce1-3378d1dc7103
                Content-Disposition: form-data; name=""
                Content-Type: application/json; charset=utf-8
                Content-Length: 32

                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                --11961578-18bf-4ce7-bce1-3378d1dc7103--

```
>３.批量上传字符串（addContent("",""，"")） Content-Disposition: form-data; name="hello"，加入了name ="hello"

```
           ItgNetSend
                .itg()
                .builder(ItgNetSend.POST)
                .url("http://test.yuanqutech.com:8080/test")
                .addContent("fasdfasdfsadfasdfasdfasdfasdfsda", ItgNetSend.MEDIA_JSON)
                .addContent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","hello",ItgNetSend.MEDIA_JSON)
                .send(new ItgCallback() {
                    @Override
                    public void onFailure(String er) {
                        ItgLog.e(er);
                    }

                    @Override
                    public void onResponse(String result, int code) {
                        ItgLog.e(result);
                    }
                });

                {t:"test",off:1790844,p:0,msgid:73619198984676866,msgtime:"2019/06/27 11:09:06.748",serverid:1,size:415,r:0,d:0}�--330f01c2-895c-4936-9316-7f4f0e45ba95
                Content-Disposition: form-data; name=""
                Content-Type: application/json; charset=utf-8
                Content-Length: 32

                fasdfasdfsadfasdfasdfasdfasdfsda
                --330f01c2-895c-4936-9316-7f4f0e45ba95
                Content-Disposition: form-data; name="hello"
                Content-Type: application/json; charset=utf-8
                Content-Length: 32

                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                --330f01c2-895c-4936-9316-7f4f0e45ba95--


```
#### ItgNetSend 上传文件（如果需要批量上传文件，调用多次addFile(new File())）

```
      ItgNetSend
                .itg()
                .builder(ItgNetSend.POST)
                .url("http://test.yuanqutech.com:8080/test")
                .addFile(new File(()))
                .send(new ItgCallback() {
                    @Override
                    public void onFailure(String er) {
                        ItgLog.e(er);
                    }

                    @Override
                    public void onResponse(String result, int code) {
                        ItgLog.e(result);
                    }
                });
```

   #### ItgNetSend 下载apk，zip，txt文件

>下载网络数据，从当前activity中退出后，下载任务继续在后台下载，并且保存了销毁activity的引用，
直到下载任务完成或下载失败才能释放activity，退出时调用取消下载api，可以快速释放activity引用，
退出时可以调用task.cancel("http://robot.yuanqutech.com:8030/ver/download?id=205")
或者 ItgNetSend.itg().itgDownlad().cancel("http://robot.yuanqutech.com:8030/ver/download?id=205");
     取消下载
```
Task task = ItgNetSend.itg()
            .itgDownlad()//获取网络下载工具
            .initTask()//初始化任务
            .url("http://robot.yuanqutech.com:8030/ver/download?id=205")//设置下载地址
            .path(Environment.getExternalStorageDirectory() + "/cd/download.apk")//下载数据保存路径
            .append(true)//启用断点续传(支持断点续传，不然会下载失败)
            .broadcast(true)//现在完成后，调用完成广播
            .broadcastComponentName("com.yqtec.sesame.composition.common.broadcast.ApkInstallBroadcast")////android9必须传入
            .callback(new ItgProgressback() {
                @Override
                public void itgProgress(ItgTask task) {
                }

                @Override
                public void fail(String error, String url) {
                    ItgLog.e(error);
                }
            }).task();
        ItgNetSend.itg().itgDownlad().start(task);

```
>如果需要在退出activity不取消下载，并且在返回activity后可以监听退出前下载任务的下载进度，使用可恢复下载回掉。【可恢复监听器，在退出activity时，不会保存activity的引用】


```
Task task = ItgNetSend.itg()
            .itgDownlad()//获取网络下载工具
            .initTask()//初始化任务
            .url("http://robot.yuanqutech.com:8030/ver/download?id=205")//设置下载地址
            .path(Environment.getExternalStorageDirectory() + "/cd/download.apk")//下载数据保存路径
            .append(true)//启用断点续传(支持断点续传，不然会下载失败)
	    .broadcast(true)//现在完成后，调用完成广播
            .broadcastComponentName("com.yqtec.sesame.composition.common.broadcast.ApkInstallBroadcast")////android9必须传入
            .registerCallback()//设置可恢复监听
            .task();

ItgNetSend.itg().itgDownlad().start(task);

//注册一个恢复监听器
ItgNetSend
         .itg()
         .callbackMgr()
         .addItgProgress("http://robot.yuanqutech.com:8030/ver/download?id=205", new ItgProgressback() {

            @Override
            public void itgProgress(ItgTask task) {

            }

            @Override
            public void fail(String error, String url) {

            }
        });

//finish activity时调用
ItgNetSend.itg().callbackMgr().removeItgProgress("http://robot.yuanqutech.com:8030/ver/download?id=205");


```

> 如果创建下载任务时，启用广播，则下载完成后，后启用下面广播(action不能修改)

```
   <receiver
            android:name=".common.broadcast.ApkInstallBroadcast"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="com.yqtec.install.broadcast" />
            </intent-filter>
        </receiver>

```

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
    	implementation 'com.github.luozhiran:ItgNetSend:1.0.3'
    	 implementation 'com.squareup.okhttp3:okhttp:3.14.2'
}
```
