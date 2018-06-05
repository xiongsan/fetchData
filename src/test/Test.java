import com.fable.enclosure.bussiness.entity.FileRelation;
import com.fable.enclosure.bussiness.interfaces.BaseRequest;
import com.fable.enclosure.bussiness.interfaces.BaseResponse;
import com.fable.enclosure.bussiness.service.impl.BaseServiceImpl;
import com.fable.enclosure.bussiness.util.ResultKit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/5/24
 * Time :11:38
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class Test extends BaseServiceImpl{

    public BaseResponse test(BaseRequest<FileRelation> param){
        System.out.println(param.getParam().getFileName());
        return ResultKit.serviceResponse(param);
    }

    public static void main(String[] args) throws IOException {
        String param = "{\"pageNo\":1,\"pageSize\":10,\"method\":\"test\",\"param\":{\"fileName\":\"1234\"}}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node=(ObjectNode)mapper.readTree(param);



        System.out.println(new Test().service(node));
    }
}
