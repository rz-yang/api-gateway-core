package personal.rezy.bind;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 代理工厂类
public class GenericReferenceProxyFactory {
    /**
     * RPC泛化调用
     */
    private final GenericService genericService;

    private final Map<String, IGenericReference> genericReferenceCache = new ConcurrentHashMap<>();

    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }

    // 本质上在接口上进行代理，转成RPC调用，返回对应的（继承实现其接口的）代理类
    public IGenericReference newInstance(String method) {
        // 如果对应method存在直接用cache返回，否则计算得到对应的IGenericReference
        // 本质上就是对第一次调用的方法缓存其对应的IGenericReference
        return genericReferenceCache.computeIfAbsent(method, k -> {
            // 泛化调用
            // 创建代理类（genericService共用）
            GenericReferenceProxy genericReferenceMethodInterceptor = new GenericReferenceProxy(genericService, method);
            // 创建接口
            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(new Signature(method, Type.getType(String.class), new Type[]{Type.getType(String.class)}), null);
            Class<?> interfaceClass = interfaceMaker.create();
            // 代理对象：Object
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            // IGenericReference 统一泛化调用接口
            // interfaceClass 根据泛化调用注册信息创建的接口，建立 http -> rpc 关联
            enhancer.setInterfaces(new Class[]{IGenericReference.class, interfaceClass});
            enhancer.setCallback(genericReferenceMethodInterceptor);
            // 真正的代理对象
            Object object = enhancer.create();
            IGenericReference iGenericReference = (IGenericReference) object;
            return iGenericReference;
        });
    }
}
