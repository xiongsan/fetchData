# fetchData
一个controller和service的封装方便前台调用和后台处理

2  js
/参数中未定义为同步还是异步默认为同步，定义之后就为false
let ajaxService = function (url, param,async,callback) {
    const _param = JSON.stringify(param)
    $.ajax({
        url: url,
        type: 'POST',
        async:async===undefined,
        dataType: 'json',
        data: { data: _param },
        success:function (data) {
            callback(data)
        }
    })
}

function fableService(){
    const serviceId=arguments[0]
    const method=arguments[1]
    let url = "fableService?"
    url += "serviceId=" + serviceId
    url += "&method=" + method
    const thirdParam= arguments[2]
    if(typeof thirdParam==='function'){
        ajaxService(url,undefined,undefined,arguments[2])
        return
    }
    if(typeof thirdParam==='boolean'){
        ajaxService(url,undefined,thirdParam,arguments[3])
        return
    }
    if(typeof arguments[3]==='boolean'){
        ajaxService(url,thirdParam,arguments[3],arguments[4])
        return
    }
    ajaxService(url,thirdParam,undefined,arguments[3])
}

function upload(){
	   $("#upload").click(function () {
            var formData = new FormData();
            formData.append("file", document.getElementById("file1").files[0]);
            $.ajax({
                url: $.base+"/loginController/upload",
                type: "POST",
                data: formData,
                contentType: false,
                processData: false,
                success: function (e) {
                    if (e.status ==='1') {
                    	var map=e.data;
                    	console.log(map,'----------')
                        alert("上传成功！");
                    }
                },
                error: function () {
                    alert("上传失败！");
                }
            });
        });
}

3 dependency

<dependency>
       <groupId>com.fable.enclosure</groupId>
         <artifactId>ssc</artifactId>
      <version>1.0</version>
      </dependency>
	  
4	 spring 注册bean
	  
<bean name="enclosureBeanUtil" class="com.fable.enclosure.bussiness.util.SpringContextUtil" />


5 service 继承 BaseServiceImpl
