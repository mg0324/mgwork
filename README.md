# mgwork
自定义mvc框架，基于mipo frameworkServlet思想。

##优点
1.是直接基于底层的servlet来处理跳转的，所以速度最接近servlet，是轻量级的mvc控制层框架，效率高。<br/>
2.表单参数转json,map,object都是基于json的实现包fastjson来实现的（fastjson也据成市效率最高的json序列化，反序列化最快的包）。<br/>


##功能更新
###2016-5-2
1.基于Servlet3.0特性，注解配置，加上action参数实现点对点控制跳转。<br/>
2.加入表单参数提交到后台的封装，目前支持mgf2Object(表单参数转对象),mgf2Json(表单参数转json),mgf2Map(表单参数转map)三种封装。<br/>
3.将文件存放位置，网页文件后缀，请求方法名称，都放到配置文件中。
