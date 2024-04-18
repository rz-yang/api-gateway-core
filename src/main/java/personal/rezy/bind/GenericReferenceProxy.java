package personal.rezy.bind;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;

/**
 * cglib动态代理拦截
 * 代理类代理具体方法的调用
 * 借助Dubbo来远程调用方法（不然是单纯的本地代理调用，借助Dubbo->远程过程调用）
 */
public class GenericReferenceProxy implements MethodInterceptor {
    /**
     * RPC 泛化调用服务
     */
    private final GenericService genericService;
    /**
     * RPC 泛化调用方法名
     */
    private final String methodName;

    public GenericReferenceProxy(GenericService genericService, String methodName) {
        this.genericService = genericService;
        this.methodName = methodName;
    }

    // cglib动态代理
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 获取函数入参的类型名称
        String[] parameters = new String[parameterTypes.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = parameterTypes[i].getName();
        }
        // genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"world"});
        // dubbo泛化调用，方法名+入参类型+入参值
        Object result = genericService.$invoke(methodName, parameters, objects);
        return result;
    }
}
