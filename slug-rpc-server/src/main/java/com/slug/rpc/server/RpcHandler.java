package com.slug.rpc.server;

import com.slug.rpc.common.RpcRequest;
import com.slug.rpc.common.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.Map;

/**
 * @author zhw
 * @version 0.1  15/11/4
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private final Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

        RpcResponse response = new RpcResponse();
        response.setRequestId(response.getRequestId());
        try {
            Object result = handle(rpcRequest);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
        }

        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }


    private Object handle(RpcRequest rpcRequest) throws Throwable {
        String className = rpcRequest.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }


}
