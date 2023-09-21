	package top.fols.aapp.bytebuddys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.implementation.bind.annotation.*;

//test
public class MainActivity extends Activity {
	
	
	
	public static abstract class InterfaceInterceptor implements InvocationHandler {
		public InterfaceInterceptor() {}
		
		
		@Override
		public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
	}
	
	public static abstract class ConstructorInterceptor {
		public abstract Object intercept(
		Object obj, // 目标对象
		Object[] allArguments// 注入目标方法的全部参数
		) throws Throwable;
	}
	
	public static abstract class MethodInterceptor {
		public abstract Object intercept(
		Callable<?> superCall,
		Object superObject,
		Object thisObject,
		Method method,
		Object[] allArguments
		) throws Throwable;
	}
	
	
	@SuppressWarnings("UnnecessaryModifier")
	public static interface DynamicTypeLoader {
		public DynamicType.Loaded<?> load(ClassLoader classLoader, DynamicType.Unloaded<?> unloaded) throws Throwable;
	}

	protected static class BeforeConstructorInterceptor {
		ConstructorInterceptor constructorInterceptor;

		public BeforeConstructorInterceptor(ConstructorInterceptor constructorInterceptor) {
			this.constructorInterceptor = constructorInterceptor;
		}

		@RuntimeType
		public Object intercept(@This Object obj, // 目标对象
				@AllArguments Object[] allArguments// 注入目标方法的全部参数
		) throws Throwable {
			return constructorInterceptor.intercept(obj, allArguments);
		}
	}

	public static class BeforeMethodInterceptor {
		MethodInterceptor methodInterceptor;

		public BeforeMethodInterceptor(MethodInterceptor methodInterceptor) {
			this.methodInterceptor = methodInterceptor;
		}

		@RuntimeType
		public Object intercept(@SuperCall Callable<?> superCall, @Super Object superObject, @This Object thisObject,
				@Origin Method method, @AllArguments Object[] allArguments) throws Throwable {
			return methodInterceptor.intercept(superCall, superObject, thisObject, method, allArguments);
		}
	}
	
	
	
	public static interface INew{
		public int get();
	}
	public static class New implements INew{
		@Override
		public int get() {
			return 666;
		};
	}
	public static DynamicType.Unloaded create(ClassLoader classLoader, Class<?> subclass, Class<?>[] interfaces,

			ConstructorInterceptor constructorInterceptor, MethodInterceptor methodInterceptor,
			InvocationHandler interfaceInterceptor) {
		    DynamicType.Unloaded unloaded = new ByteBuddy()
				.subclass(subclass).implement(interfaces)

				.constructor(ElementMatchers.any()).intercept(SuperMethodCall.INSTANCE.andThen(
						// 执行完原始构造方法，再开始执行interceptor的代码
						MethodDelegation.withDefaultConfiguration()
								.to(new BeforeConstructorInterceptor(constructorInterceptor))

				))

				.method(ElementMatchers.isAbstract())
				.intercept(InvocationHandlerAdapter.of(interfaceInterceptor))

				.method(ElementMatchers.not(ElementMatchers.isAbstract()))
				.intercept(MethodDelegation.to(new BeforeMethodInterceptor(methodInterceptor)))

				.method(ElementMatchers.isDefaultMethod())
				.intercept(InvocationHandlerAdapter.of(interfaceInterceptor))

				.make();

		return unloaded;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView textView = findViewById(R.id.aaa);
		
		ClassLoader classloader = MainActivity.class.getClassLoader();
		try {
			Object object = create(classloader,
			New.class, new Class[]{INew.class, List.class},
			new ConstructorInterceptor() {
				public Object intercept(
				Object obj, // 目标对象
				Object[] allArguments// 注入目标方法的全部参数
				) throws Throwable {
					textView.setText("constructor()");
			        textView.append("\n");
			        return null;
				}
			},
			new MethodInterceptor() {
				@Override
				public Object intercept(Callable<?> superCall, Object superObject, Object thisObject, Method method, Object[] allArguments) throws Throwable {
					textView.setText(method.toString());
			        textView.append("\n");
			        
					return superCall.call();
				}
			},
			new InterfaceInterceptor() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				    textView.setText(method.toString());
			        textView.append("\n");
			        return null;
				}
			});
			
			textView.setText(object.toString());
			textView.append("\n");
		} catch (Throwable e) {
			StringWriter sw=new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			
			textView.setText(sw.toString());
		}
		
	}
}