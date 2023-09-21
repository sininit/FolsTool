package top.fols.box.reflect.re;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import top.fols.atri.interfaces.annotations.Nullable;
import top.fols.box.reflect.re.Re_CodeLoader.Base;
import top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker;
import top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts_MyCVF.MyFunction;

import static top.fols.box.reflect.re.Re_Modifiers.*;
import static top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker.TYPECHECKER_FUNCTION_TYPE_CHECK;
/*safe*/import static top.fols.box.reflect.re.Re_Variable.createDynamicCallAsExpression;

@SuppressWarnings("rawtypes")
public class Re_PrimitiveClassMyCVF {
	static Set<String> parseModifiersAsSet(String modifier) {
		Set<String> set = new LinkedHashSet<>();
		if (null == modifier) {
			return set;
		} else {
			StringTokenizer st = new StringTokenizer(modifier);
			while (st.hasMoreElements()) {
				set.add(st.nextToken());
			}
			return set;
		}
	}
	public static int parseModifier(String modifier, Re_Modifiers.ModifierVerifier mv) {
		if (null == modifier) {
			return EMPTY_MODIFIER;
		} else {
			Set<String> set = parseModifiersAsSet(modifier);
			String check = mv.testModifier(set);
			if (null == check) {
				return Re_Modifiers.parseModifier(set);
			} else {
				throw new UnsupportedOperationException(check);
			}
		}
	}




	public static class InitInstaller {
		Re_PrimitiveClass pClass;
		int modifier;
		IFunction function;
		int line;

		public InitInstaller(Re_PrimitiveClass onClass) {
			this.pClass = onClass;
		}


		public InitInstaller modifier(int modifier) {
			return this.modifier(Re_Modifiers.asString(modifier));
		}
		public InitInstaller modifier(String modifier) {
			this.modifier = parseModifier(modifier, INIT_MV);
			return this;
		}

		public InitInstaller line(int line)	{
			this.line = line;
			return this;
		}

		public InitInstaller callback(IFunction function) {
			this.function = function;
			return this;
		}

		public int getFunctionModifier() {
            return MyFunction.getFunctionModifier(false, modifier);
        }

		void check() {
			if (null == pClass)
				throw new RuntimeException("null class");
			if (modifier < 0)
				throw new RuntimeException("error modifier");
			if (null == function)
				throw new RuntimeException("no set callback");
			if (line <= 0)
				throw new RuntimeException("no set line");

			if (null != function.bind)
				throw new RuntimeException("callback already bind");
		}

		Re_ClassFunction createFunction() {
			Re_ClassFunction rcf = Re_PrimitiveClassMyCVF.createFunction(pClass, line,
																getFunctionModifier(), Re_Keywords.INNER_EXPRESSION_CALL__SET_INIT_FUNCTION, function);
			function.bind = rcf;
			return rcf;
		}


		public static void install(Re_PrimitiveClass pClass, int line,
								   String modifier, IFunction ifun) {
			InitInstaller installer = new InitInstaller(pClass);
			installer.modifier(modifier);
			installer.line(line);
			installer.callback(ifun);
			installer.install();
		}
		public void install() {
			check();

			final Re_Class reClass = pClass;
			final int functionModifier = getFunctionModifier();

			boolean onClassAtInitialization = false;
			if (reClass.isInitialing()) {
				if (onClassAtInitialization) {
					throw new UnsupportedOperationException();
					//Access Modifier
				} else {
					//static
					Re_ClassFunction value = createFunction();

					pClass.setInitFunction(value);
					return;
				}
			} else {
				throw new RuntimeException(Re_Accidents.reclass_initialized(reClass));
			}
		}
	}

	@SuppressWarnings("UnnecessaryLocalVariable")
	public static class VarInstaller {
		Re_PrimitiveClass pClass;
		int modifier;
		String name;
		Object  setValue;
		boolean isSetValue;
		int line;
		TypeChecker typeChecker;

		public VarInstaller(Re_PrimitiveClass onClass) {
			this.pClass = onClass;
		}

		public VarInstaller modifier(int modifier) {
			return this.modifier(Re_Modifiers.asString(modifier));
		}
		public VarInstaller modifier(String modifier) {
			this.modifier = parseModifier(modifier, Re_Modifiers.VAR_MV);
			return this;
		}

		public VarInstaller name(String name) {
			if (Re_Keywords.is_keyword_key(name)) {
				throw new UnsupportedOperationException(name + " is keyword");
			}
			this.name = Re_CodeLoader.intern(name);
			return this;
		}

		public VarInstaller line(int line)	{
			this.line = line;
			return this;
		}

		public VarInstaller type(TypeChecker type) {
			this.typeChecker = type;
			return this;
		}

		public VarInstaller value(Object value) {
			this.setValue = value;
			this.isSetValue = true;
			return this;
		}

		public int getVariableModifier() {
            return modifier;
        }


		void check() {
			if (null == pClass)
				throw new RuntimeException("null class");
			if (modifier < 0)
				throw new RuntimeException("error modifier");
			if (null == name || name.length() <= 0)
				throw new RuntimeException("null function name");
			if (line <= 0)
				throw new RuntimeException("no set line");
		}

		VariableBuilder toVariableBuilder() {
			final int variableModifier = getVariableModifier();
            boolean hasConst = !isVariableRemovable(variableModifier);
            boolean hasTypes = null != typeChecker;

            if (hasTypes) {
                if (hasConst) {
                    return new VariableBuilder() {
                        @Override
                        public Re_Variable build() {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_Final_TypeRequired(variableModifier, typeChecker);
                        }
                    };
                } else {
                    return new VariableBuilder() {
                        @Override
                        public Re_Variable build() {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_TypeRequired(variableModifier, typeChecker);
                        }
                    };
                }
            } else {
                if (hasConst) {
                    return new VariableBuilder() {
                        @Override
                        public Re_Variable build() {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_Final(variableModifier);
                        }
                    };
                } else {
                    return new VariableBuilder() {
                        @Override
                        public Re_Variable build() {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_(variableModifier);
                        }
                    };
                }
            }
		}



		public static void install(Re_PrimitiveClass pClass, int line,
								   String modifier, String name, TypeChecker type) {
			VarInstaller installer = new VarInstaller(pClass);
			installer.modifier(modifier);
			installer.name(name);
			installer.line(line);
			installer.type(type);
			installer.install();
		}
		public static void install(Re_PrimitiveClass pClass, int line,
								   String modifier, String name, TypeChecker type, Object value) {
			VarInstaller installer = new VarInstaller(pClass);
			installer.modifier(modifier);
			installer.name(name);
			installer.line(line);
			installer.type(type);
			installer.value(value);
			installer.install();
		}
		public void install() {
			check();

			final Re_Class reClass = pClass;
			final VariableBuilder variableBuilder  = toVariableBuilder();
			final int 			  variableModifier = getVariableModifier();

            boolean isStatic = Re_Modifiers.isStatic(variableModifier);
            boolean isThis   = Re_Modifiers.isThis(variableModifier);
            boolean isStruct = Re_Modifiers.isStruct(variableModifier);
            boolean onStruct = isStatic || isThis || isStruct;

            boolean onClassAtInitialization = onStruct;
            if (onClassAtInitialization) {
				if (reClass.isInitialing()) {
					if (isStatic) {
						Re_Variable variable = variableBuilder.build();
						Re_Variable.Unsafes.addVariableInternOrThrowEx(name, variable, reClass);

						if (isSetValue) {
							Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(variable, setValue);
						}
					} else if (isThis) {
						//类初始化时 将表达式添加到实例初始化表达式列表
						/*safe*/reClass.addInstanceInitializationStatementExpression(createDynamicCallAsExpression(line,
							"<primitive-var-init:" + line + ">",
								/*safe*/new Re_Variable.Re_Variable_DynamicCall.DynamicCall() {
								@Override
								public Object execute(Re_Executor executor) {
									//this
									Re_ClassInstance reClassInstance = executor.getReClassInstance();
									if (null == reClassInstance) {
										executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
										return null;
									}

									Re_Variable variable = variableBuilder.build();
									Re_Variable.accessPutNewVariableRequire(executor,
																			name, variable,
																			reClassInstance);
									if (executor.isReturnOrThrow()) return null;

									if (isSetValue) {
										Re_Variable.accessSetValue(executor, variable, name, setValue);
										return setValue;
									}
									return null;
								}
							}));
					} else if (isStruct) {
						Re_Variable variable = variableBuilder.build();
						Re_Variable.Unsafes.addVariableInternOrThrowEx(name, variable, reClass);

						if (isSetValue) {
							Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(variable, setValue);
						}

						//类初始化时 将表达式添加到实例初始化表达式列表
						/*safe*/reClass.addInstanceInitializationStatementExpression(createDynamicCallAsExpression(line,
								"<primitive-var-init:" + line + ">",
								/*safe*/new Re_Variable.Re_Variable_DynamicCall.DynamicCall(){
									@Override
									public Object execute(Re_Executor executor) {
										//this
										Re_ClassInstance reClassInstance = executor.getReClassInstance();
										if (null == reClassInstance) {
											executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
											return null;
										}
										Re_Variable.UnsafesRe.setInstanceVariableFromReClass(executor,
												reClass, reClassInstance,
												name);
										return null;
									}
								}));
					} else {
						throw new UnsupportedOperationException(Re_Modifiers.asString(variableModifier));
						//必须在类初始化的时候进行
					}
				} else {
					throw new RuntimeException(Re_Accidents.reclass_initialized(reClass));
				}
            } else {
                //local var
                Re_Variable variable = variableBuilder.build();
				Re_Variable.Unsafes.addVariableInternOrThrowEx(name, variable, reClass);

				if (isSetValue) {
					Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(variable, setValue);
				}
            }
		}
	}


	@SuppressWarnings("UnnecessaryLocalVariable")
	public static class FunctionInstaller {
		Re_PrimitiveClass pClass;
		int modifier;
		String name;
		IFunction function;
		int line;

		public FunctionInstaller(Re_PrimitiveClass onClass) {
			this.pClass = onClass;
		}

		public FunctionInstaller modifier(int modifier) {
			return this.modifier(Re_Modifiers.asString(modifier));
		}
		public FunctionInstaller modifier(String modifier) {
			this.modifier = parseModifier(modifier, Re_Modifiers.FUNCTION_MV);
			return this;
		}

		public FunctionInstaller name(String name) {
			if (Re_Keywords.is_keyword_key(name)) {
				throw new UnsupportedOperationException(name + " is keyword");
			}
			this.name = Re_CodeLoader.intern(name);
			return this;
		}

		public FunctionInstaller line(int line)	{
			this.line = line;
			return this;
		}

		public FunctionInstaller callback(IFunction function) {
			this.function = function;
			return this;
		}

		public int getVariableModifier() {
            return MyFunction.getVariableModifier(false, modifier);
        }
		public int getFunctionModifier() {
            return MyFunction.getFunctionModifier(false, modifier);
        }

		void check() {
			if (null == pClass)
				throw new RuntimeException("null class");
			if (modifier < 0)
				throw new RuntimeException("error modifier");
			if (null == name || name.length() <= 0)
				throw new RuntimeException("null function name");
			if (null == function)
				throw new RuntimeException("no set callback");
			if (line <= 0)
				throw new RuntimeException("no set line");

			if (null != function.bind)
				throw new RuntimeException("callback already bind");
		}


		VariableBuilder toVariableBuilder() {
			final int variableModifier = getVariableModifier();
			boolean hasConst = !isVariableRemovable(variableModifier);
			if (hasConst) {
				return new VariableBuilder(){
					@Override
					public Re_Variable build() {
						// TODO: Implement this method
						return Re_Variable._newModifierVariable_Final_TypeRequired(variableModifier, TYPECHECKER_FUNCTION_TYPE_CHECK);
					}
				};
			} else {
				return new VariableBuilder(){
					@Override
					public Re_Variable build() {
						// TODO: Implement this method
						return Re_Variable._newModifierVariable_(variableModifier);
					}
				};
			}
		}




		Re_ClassFunction createFunction() {
			Re_ClassFunction rcf = Re_PrimitiveClassMyCVF.createFunction(pClass, line,
																getFunctionModifier(), name, function);
			function.bind = rcf;
			return rcf;
		}


		public static void install(Re_PrimitiveClass pClass, int line,
								   String modifier, String name, IFunction ifun) {
			Re_PrimitiveClassMyCVF.FunctionInstaller installer = new Re_PrimitiveClassMyCVF.FunctionInstaller(pClass);
			installer.modifier(modifier);
			installer.name(name);
			installer.line(line);
			installer.callback(ifun); 
			installer.install();
		}
		public void install() {
			check();

			final int functionModifier = getFunctionModifier();
			final Re_Class reClass = pClass;
			final VariableBuilder variableBuilder = toVariableBuilder();

			boolean isStruct = isStruct(functionModifier);
			boolean onClassAtInitialization = isStruct;
			if (onClassAtInitialization) {
				// TODO: Implement this method
				if (reClass.isInitialing()) {
					if (isStruct) {
						//类初始化时 将表达式添加到实例初始化表达式列表
						/*safe*/reClass.addInstanceInitializationStatementExpression(createDynamicCallAsExpression(line,
							"<primitive-function-init:" + line + ">",
								/*safe*/new Re_Variable.Re_Variable_DynamicCall.DynamicCall(){
								@Override
								public Object execute(Re_Executor executor) {
									//this
									Re_ClassInstance reClassInstance = executor.getReClassInstance();
									if (null == reClassInstance) {
										executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
										return null;
									}
									Re_Variable.UnsafesRe.setInstanceVariableFromReClass(executor,
											reClass, reClassInstance,
											name);
									return null;
								}
							}));

						Re_Variable variable = variableBuilder.build();
						Re_Variable.Unsafes.addVariableInternOrThrowEx(name, variable, reClass);

						Object value = createFunction();

						Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(variable, value);
					} else {
						throw new UnsupportedOperationException();
						//Access Modifier
					}
				} else {
					throw new RuntimeException(Re_Accidents.reclass_initialized(reClass));
				}
			} else {
				Re_Variable variable = variableBuilder.build();
				Re_Variable.Unsafes.addVariableInternOrThrowEx(name, variable, reClass);

				Object value = createFunction();

				Re_Variable.Unsafes.fromUnsafeAccessorSetValueOrThrowEx(variable, value);
			}
		}
	}

	static abstract class VariableBuilder {
		public abstract Re_Variable build();
	}

	public static abstract class IInit extends IFunction {
		public IInit() {
			super(Re_Keywords.INNER_EXPRESSION_CALL__SET_INIT_FUNCTION);
		}
	}
	public static abstract class IFunction {
		protected String name;
		public IFunction(String name) {
			this.name = name;
		}

		protected Re_ClassFunction bind;
		public abstract Object unauthenticationExecuteOnNative(Re_Executor executor,
															   Re_Class runInClass, Re_ClassInstance runInInstance,
															   Object[] arguments) throws Throwable;
	}


	static Re_ClassFunction createFunction(
		final Re_Class pClass, final int line,
		final int functionModifier,
		final String functionName,
		final IFunction callback) {

		final Re_CodeFile classFile = pClass.getCodeBlock();
		final String filePath       = classFile.getFilePath();

		Re_PrimitivesClassFunction reFunction = new Re_PrimitivesClassFunction(callback);
		String buildFunctionName = Re_ClassFunction.buildReClassFunctionName(line, functionName, reFunction);
		Re_CodeFile block 		 = Re_CodeFile.create(classFile.getConstCaches(),
													  buildFunctionName, filePath, line,
													  Base.EMPTY_EXPRESSION);
		return Re_ClassFunction.createReCassFunctionAfter(reFunction,
			block,
			functionModifier,
			buildFunctionName, null,
			null,
			pClass, null);
	}

	private static class Re_PrimitivesClassFunction extends Re_PrimitiveClassFunction {
		private final IFunction callback;
		public Re_PrimitivesClassFunction(IFunction callback) {
			this.callback = callback;
		}

		@Override
		protected Object unauthenticationExecuteOnNative(Re_Executor executor,
														 Re_Class runInClass, Re_ClassInstance runInInstance,
														 Object[] arguments, @Nullable Re_IRe_VariableMap functionLocal) throws Throwable {
//			Re_NativeStack strack = executor.getStack();
//			Re_NativeStack.ReNativeTraceElement nt = new Re_NativeStack.ReNativeTraceElement(executor, callback.bind.getReCodeBlock().getFilePath(), callback.bind.getReCodeBlock().getLineOffset());
//			int ntIndex = strack.addStackElementAndGetIndex(nt);

			Object value = callback.unauthenticationExecuteOnNative(executor, runInClass, runInInstance,
																	arguments);
			if (executor.isReturnOrThrow()) return null;

//			strack.removeStackElementFromIndex(ntIndex, nt);

			return value;
		}
	}
}
