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
        .url("http://www.baidu.com");//输入全局地址，设置此项后，在使用网络请求时，没有输入地址，则使用该地址请求
    }
}
        
```

#### ItgNetSend请求
        
```
     使用配置中配置的地址
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
                
       使用传入地址         
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
   
   #### ItgNetSend 下载apk，zip，txt文件
   
```
     下载网络数据，从当前activity中退出后，下载任务继续在后台下载，并且保存了销毁activity的引用，
     直到下载任务完成或下载失败才能释放activity，退出时调用取消下载api，可以快速释放activity引用，
     退出时可以调用task.cancel("http://robot.yuanqutech.com:8030/ver/download?id=205")
     或者 ItgNetSend.itg().itgDownlad().cancel("http://robot.yuanqutech.com:8030/ver/download?id=205");
     取消下载
  
                Task task = ItgNetSend.itg()
                .itgDownlad()//获取网络下载工具
                .initTask()//初始化任务
                .url("http://robot.yuanqutech.com:8030/ver/download?id=205")//设置下载地址
                .path(Environment.getExternalStorageDirectory() + "/cd/download.apk")//下载数据保存路径
                .append(true)//启用断点续传(支持断点续传，不然会下载失败)
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
        
        如果需要在退出activity不取消下载，并且在返回activity后可以监听退出前下载任务的下载进度，使用可恢复下载回掉
   
           Task task = ItgNetSend.itg()
                .itgDownlad()//获取网络下载工具
                .initTask()//初始化任务
                .url("http://robot.yuanqutech.com:8030/ver/download?id=205")//设置下载地址
                .path(Environment.getExternalStorageDirectory() + "/cd/download.apk")//下载数据保存路径
                .append(true)//启用断点续传(支持断点续传，不然会下载失败)
                .registerCallback()//设置可恢复监听
                .task();
        
            ItgNetSend.itg().itgDownlad().start(task);
            
          //注册一个恢复监听器
            ItgNetSend.itg().callbackMgr().addItgProgress("http://robot.yuanqutech.com:8030/ver/download?id=205", new ItgProgressback() {
            @Override
            public void itgProgress(ItgTask task) {
                
            }

            @Override
            public void fail(String error, String url) {

            }
        });
        
        //finish activity时调用
         ItgNetSend.itg().callbackMgr().removeItgProgress("http://robot.yuanqutech.com:8030/ver/download?id=205");
        
        
        可恢复监听器，在退出activity时，不会保存activity的引用
        
```
