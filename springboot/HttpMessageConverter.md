# **HttpMessageConverter**

* @RequestBody、@ResponseBody注解，可以直接将输入解析成Json、将输出解析成Json，但HTTP 请求和响应是基于文本的，意味着浏览器和服务器通过交换原始文本进行通信，而这里其实就是HttpMessageConverter发挥着作用。

* Http请求响应报文其实都是字符串，当请求报文到java程序会被封装为一个ServletInputStream流，开发人员再读取报文，响应报文则通过ServletOutputStream流，来输出响应报文。



