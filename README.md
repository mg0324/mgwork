# mgwork-ioc
自定义mvc框架，基于mipo frameworkServlet思想。<br/>
整合mgioc框架（自定义的一款小巧的ioc框架）后，生成mgwork-ioc框架；拥有健全的mvc控制，ioc依赖注入特性。

##优点
1.是直接基于底层的servlet来处理跳转的，所以速度最接近servlet，是轻量级的mvc控制层框架，效率高。<br/>
2.表单参数转json,map,object都是基于json的实现包fastjson来实现的（fastjson也据成市效率最高的json序列化，反序列化最快的包）。<br/>
3.常用的getPara,getAttr,renderJsp,renderFreemarker,ajaxJsonSuccess,ajaxJsonError等方法。<br/>

##依赖jar包
1.freemarker jar（可选），如果使用到freemarker模板的话。<br/>
2.fastjson jar（必须），表单数据封装为对象，需要json包。<br/>

##功能更新
###2016-5-2
1.基于Servlet3.0特性，注解配置，加上action参数实现点对点控制跳转。<br/>
2.加入表单参数提交到后台的封装，目前支持mgf2Object(表单参数转对象),mgf2Json(表单参数转json),mgf2Map(表单参数转map)三种封装。<br/>
3.将文件存放位置，网页文件后缀，请求方法名称，都放到配置文件中。<br/>
4.添加jsp 案例。<br/>
5.添加freemarker视图支持。<br/>

###2016-5-3
1.添加多模板jsp,freemarker支持，return "d/demo";支持默认模板配置。<br/>
2.增强request,response的相关方法。getPara,renderJson等。<br/>
3.添加项目路径ROOT支持，可以在jsp和freemarker中使用${ROOT}，特别在引入资源文件时需要使用到。<br/>
4.支持url路径从http://localhost:8080/mgwork/test.mg?action=test1 更新为 http://localhost:8080/mgwork/test.mg/test1(更普遍化)。
注意点：可以去掉冗余配置<br/>
-》请求方法的参数 mgwork.web.req.method = action<br/>
要注意的是在配置Servlet3.0的时候，需要从之前的<br/>
@WebServlet("/test.mg") 更新为 @WebServlet("/test.mg/*")加上/*标示支持test.mg为前缀的路径。

###2016-5-4
1.整合mgioc到mgwork框架中，生成mgwork-ioc框架。<br/>
2.优化mgwork的bug.(getActionNameFromUrl 中 request可能未空，导致启动时报错。)<br/>
3.修复mgioc扫描包路径，从mgwork.properties文件中获取mgioc.scan.package，如果没有配置，就默认所有包。<br/>
4.修复mgwork的Action是交个servlet3.0管理的(请求对应，非单例)，所以不用mgioc管理的bug.<br/>

###2016-5-10
1.抽出mgutil，引入依赖mgutil-1.0.jar，依赖log4j.jar来写日志。
