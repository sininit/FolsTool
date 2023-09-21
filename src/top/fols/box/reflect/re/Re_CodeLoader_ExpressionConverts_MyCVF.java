package top.fols.box.reflect.re;

import top.fols.atri.lang.Finals;
import top.fols.atri.interfaces.annotations.Nullable;

import static top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker.TYPECHECKER_FUNCTION_TYPE_CHECK;
import static top.fols.box.reflect.re.Re_CodeLoader_ExpressionConverts.CallVar.createTypeCheckerFromRuntime;
import static top.fols.box.reflect.re.Re_Modifiers.*;

@SuppressWarnings("rawtypes")
public class Re_CodeLoader_ExpressionConverts_MyCVF {
    public static MyInit createMyInit(int modifier,
                                              String[] functionParamNames, Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] functionParamTypes,
                                              Re_CodeLoader.CallCreateDict executeExpressions) {
        return new MyInit(modifier,
                functionParamNames, functionParamTypes,
                executeExpressions);
    }

    public static MyVariable createMyVariable(String name,
                                              Re_CodeLoader.Var[]  types,
                                              int modifier,
                                              Re_CodeLoader.Expression initExpression) {
        return new MyVariable(name, types, modifier, initExpression);
    }

    public static MyFunction createMyFunction(int modifier,
                                              String   functionName,
                                              String[] functionParamNames, Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] functionParamTypes,
                                              Re_CodeLoader.Var[]    functionReturnTypes,
                                              Re_CodeLoader.CallCreateDict executeExpressions) {
        return new MyFunction(modifier,
                functionName,
                functionParamNames, functionParamTypes,
                functionReturnTypes,
                executeExpressions);
    }

    protected static class MyInit {
        private final int modifier;

        final String[]            												 	functionParamNames;

        final Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] 	functionParamTypes;

        final Re_CodeLoader.CallCreateDict executeExpressions;
        Re_CodeLoader.Expression[] getExpression() {
            return executeExpressions.getBuildParamExpressionCaches();
        }



        public MyInit(int modifier, String[] functionParamNames, Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] functionParamTypes, Re_CodeLoader.CallCreateDict executeExpressions) {
            this.modifier 			 = modifier;

            this.functionParamNames  = null == functionParamNames ? Finals.EMPTY_STRING_ARRAY: functionParamNames;
            this.functionParamTypes  = functionParamTypes;
            this.executeExpressions  = executeExpressions;

            this.installer = toInstaller();
        }
        Installer 	    installer;

        public int getFunctionModifier() {
            return modifier;
        }


        public boolean hasTypes() { return null != functionParamTypes; }


        @SuppressWarnings("ConstantConditions")
        private Installer toInstaller() {
            final int functionModifier = getFunctionModifier();

            boolean onClassAtInitialization = false;
            if (onClassAtInitialization) {
                throw new UnsupportedOperationException();
                //Access Modifier
            } else {
                return new Installer(this) {
                    @Override
                    protected Object set(Re_CodeLoader.ConvertExpressionAsCallInit call, Re_Executor executor) {
                        // TODO: Implement this method
                        Re_ClassFunction function = createFunction(executor,
                                call.getLine());
                        if (executor.isReturnOrThrow()) return null;

                        Re_Class reClass = executor.reClass;
                        if (null == reClass) {
                            executor.setThrow(Re_Accidents.executor_no_bind_class());
                            return null;
                        }
                        if (reClass.isInitialized()) {
                            executor.setThrow(Re_Accidents.reclass_initialized(reClass));
                            return null;
                        }
                        reClass.setInitFunction(function);
                        return null;
                    }
                };
            }
        }


        protected static abstract class Installer {
            MyInit superz;
            public Installer(MyInit superz) {
                this.superz = superz;
            }

            protected abstract Object set(Re_CodeLoader.ConvertExpressionAsCallInit call, Re_Executor executor);

            protected Re_ClassFunction createFunction(Re_Executor executor,
                                                      int line) {
                return Re_ClassFunction.RuntimeUtils.createReFunction(line, executor,
                        superz.getFunctionModifier(),
                        Re_Keywords.INNER_EXPRESSION_CALL__SET_INIT_FUNCTION,
                        superz.functionParamNames, superz.functionParamTypes,
                        null,
                        superz.getExpression()
                );
            }

            protected MyInit superz() { return superz; }
        }
    }

    protected static class MyVariable {
        private final int modifier;
        final String name;
        final Re_CodeLoader.Var[] typeChecker;
        Re_CodeLoader.Expression initExpression;

        public MyVariable(String name, Re_CodeLoader.Var[] typeChecker, int modifier,
                          Re_CodeLoader.Expression initExpression) {
            this.name = name;
            this.typeChecker = typeChecker;
            this.modifier = modifier;
            this.initExpression = initExpression;

            this.variableBuilder = this.toVariableBuilder();
            this.installer = this.toInstaller();
        }


        VariableBuilder variableBuilder;
        Installer installer;

        public int getVariableModifier() {
            return modifier;
        }
        public boolean hasTypes() { return null != typeChecker; }



        /**
         * toBuilder 和 toInstaller 必须同时使用 两个生成的参数必须相同
         */
        private VariableBuilder toVariableBuilder() {
            final int variableModifier = getVariableModifier();
            boolean hasConst = !isVariableRemovable(variableModifier);
            boolean hasTypes = null != typeChecker;

            if (hasTypes) {
                if (hasConst) {
                    return new VariableBuilder(this) {
                        @Override
                        public Re_Variable build(Re_Executor executor) {
                            // TODO: Implement this method
                            Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker typeChecker = createTypeCheckerFromRuntime(executor, name, MyVariable.this.typeChecker);
                            return Re_Variable._newModifierVariable_Final_TypeRequired(variableModifier, typeChecker);
                        }
                    };
                } else {
                    return new VariableBuilder(this) {
                        @Override
                        public Re_Variable build(Re_Executor executor) {
                            // TODO: Implement this method
                            Re_CodeLoader_ExpressionConverts.CallFunction.TypeChecker typeChecker = createTypeCheckerFromRuntime(executor, name, MyVariable.this.typeChecker);
                            return Re_Variable._newModifierVariable_TypeRequired(variableModifier, typeChecker);
                        }
                    };
                }
            } else {
                if (hasConst) {
                    return new VariableBuilder(this) {
                        @Override
                        public Re_Variable build(Re_Executor executor) {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_Final(variableModifier);
                        }
                    };
                } else {
                    return new VariableBuilder(this) {
                        @Override
                        public Re_Variable build(Re_Executor executor) {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_(variableModifier);
                        }
                    };
                }
            }
        }
        @SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions"})
        private Installer toInstaller() {
            final int variableModifier = getVariableModifier();

            boolean isStatic = Re_Modifiers.isStatic(variableModifier);
            boolean isThis   = Re_Modifiers.isThis(variableModifier);
            boolean isStruct = Re_Modifiers.isStruct(variableModifier);
            boolean onStruct = isStatic || isThis | isStruct;

            boolean onClassAtInitialization = onStruct;
            if (onClassAtInitialization) {
                if (isStatic) {
                    return new Installer(this) {
                        @Override
                        public Object set(Re_CodeLoader.ConvertExpressionAsKeywordVar keywordVar,
                                          Re_Executor executor, String name) {
                            Re_Class reClass = executor.getReClass();
                            if (null == reClass) {
                                executor.setThrow(Re_Accidents.executor_no_bind_class());
                                return null;
                            }
                            if (reClass.isInitialized()) {
                                executor.setThrow(Re_Accidents.reclass_initialized(reClass));
                                return null;
                            }
                            Re_Variable variable = superz.variableBuilder.build(executor);
                            if (executor.isReturnOrThrow()) return null;

                            Re_Variable.accessPutNewVariableRequire(executor,
                                    name, variable,
                                    reClass);
                            if (executor.isReturnOrThrow()) return null;

                            Object value = null;
                            if (null != initExpression) {
                                value = executor.getExpressionValue(initExpression);
                                if (executor.isReturnOrThrow()) return null;

                                Re_Variable.accessSetValue(executor, variable, name, value);
                            }
                            return value;
                        }
                    };
                } else if (isThis) {
                    return new Installer(this) {
                        //类初始化的时候会执行一次
                        //类实例 init方法？ 执行的时候也会执行一次
                        @Override
                        public Object set(Re_CodeLoader.ConvertExpressionAsKeywordVar current,
                                          Re_Executor executor, final String name) {
                            int line = current.getLine();
                            final Re_Class reClass = executor.getReClass();
                            if (null == reClass) {
                                executor.setThrow(Re_Accidents.executor_no_bind_class());
                                return null;
                            }
                            if (reClass.isInitialing()) {
                                //类初始化时 将表达式添加到实例初始化表达式列表
                                /*safe*/reClass.addInstanceInitializationStatementExpression(Re_Variable.createDynamicCallAsExpression(line, name,
                                        /*safe*/new Re_Variable.Re_Variable_DynamicCall.DynamicCall() {
                                            @Override
                                            public Object execute(Re_Executor executor) {
                                                //this
                                                Re_ClassInstance reClassInstance = executor.getReClassInstance();
                                                if (null == reClassInstance) {
                                                    executor.setThrow(Re_Accidents.executor_no_bind_class_instance());
                                                    return null;
                                                }

                                                Re_Variable variable = superz.variableBuilder.build(executor);
                                                if (executor.isReturnOrThrow()) return null;

                                                Re_Variable.accessPutNewVariableRequire(executor,
                                                        name, variable,
                                                        reClassInstance);
                                                if (executor.isReturnOrThrow()) return null;

                                                Object value = null;
                                                if (null != initExpression) {
                                                    value = executor.getExpressionValue(initExpression);
                                                    if (executor.isReturnOrThrow()) return null;

                                                    Re_Variable.accessSetValue(executor, variable, name, value);
                                                }
                                                return value;
                                            }
                                        }));
                                return null;
                            } else {
                                executor.setThrow(Re_Accidents.reclass_initialized(reClass));
                                return null;
                            }
                        }
                    };
                } else if (isStruct) {
                    return new Installer(this) {
                        @Override
                        public Object set(Re_CodeLoader.ConvertExpressionAsKeywordVar current,
                                          Re_Executor executor, final String name) {
                            final int line = current.getLine();
                            final Re_Class reClass = executor.getReClass();
                            if (null == reClass) {
                                executor.setThrow(Re_Accidents.executor_no_bind_class());
                                return null;
                            }
                            if (reClass.isInitialing()) {
                                Re_Variable variable = superz.variableBuilder.build(executor);
                                if (executor.isReturnOrThrow()) return null;

                                Re_Variable.accessPutNewVariableRequire(executor,
                                        name, variable,
                                        reClass);
                                if (executor.isReturnOrThrow()) return null;

                                Object value;
                                if (null != initExpression) {
                                    value = executor.getExpressionValue(initExpression);
                                    if (executor.isReturnOrThrow()) return null;

                                    Re_Variable.accessSetValue(executor, variable, name, value);
                                }
                                if (executor.isReturnOrThrow()) return null;

                                //类初始化时 将表达式添加到实例初始化表达式列表
                                /*safe*/reClass.addInstanceInitializationStatementExpression(Re_Variable.createDynamicCallAsExpression(line, name,
                                        /*safe*/new Re_Variable.Re_Variable_DynamicCall.DynamicCall() {
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
                                return null;
                            } else {
                                executor.setThrow(Re_Accidents.reclass_initialized(reClass));
                                return null;
                            }
                        }
                    };
                } else {
                    throw new UnsupportedOperationException(Re_Modifiers.asString(variableModifier));
                    //必须在类初始化的时候进行
                }
            } else {
                //local var
                return new Installer(this) {
                    @Override
                    public Object set(Re_CodeLoader.ConvertExpressionAsKeywordVar call,
                                      Re_Executor executor,
                                      String name) {
                        // TODO: Implement this method
                        Re_Variable variable = superz.variableBuilder.build(executor);
                        if (executor.isReturnOrThrow()) return null;

                        Re_Variable.accessPutNewVariableRequire(executor,
                                name, variable, executor);
                        if (executor.isReturnOrThrow()) return null;

                        Object value = null;
                        if (null != initExpression) {
                            value = executor.getExpressionValue(initExpression);
                            if (executor.isReturnOrThrow()) return null;

                            Re_Variable.accessSetValue(executor, variable, name, value);
                        }
                        return value;
                    }
                };
            }
        }


        protected static abstract class VariableBuilder {
            MyVariable superz;
            public VariableBuilder(MyVariable superz) {
                this.superz = superz;
            }

            protected abstract Re_Variable build(Re_Executor executor);
            protected MyVariable superz() { return superz; }
        }
        protected static abstract class Installer {
            MyVariable superz;
            public Installer(MyVariable superz) {
                this.superz = superz;
            }

            protected abstract Object set(Re_CodeLoader.ConvertExpressionAsKeywordVar call,
                                          Re_Executor executor, String name);
            protected MyVariable superz() { return superz; }
        }
    }

    protected static class MyFunction {
        private final int modifier;

        final String              												 	functionName;
        final String[]            												 	functionParamNames;

        final Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] 	functionParamTypes;
        final Re_CodeLoader.Var[]                			                           		       	functionReturnTypes;

        final Re_CodeLoader.CallCreateDict executeExpressions;
        Re_CodeLoader.Expression[] getExpression() {
            return executeExpressions.getBuildParamExpressionCaches();
        }



        public MyFunction(int modifier, String functionName, String[] functionParamNames, Re_CodeLoader_ExpressionConverts.CallFunction.FunParamTypesElement[] functionParamTypes, Re_CodeLoader.Var[] functionReturnTypes, Re_CodeLoader.CallCreateDict executeExpressions) {
            this.modifier 			 = modifier;

            this.functionName 		 = functionName;
            this.functionParamNames  = null == functionParamNames ? Finals.EMPTY_STRING_ARRAY: functionParamNames;
            this.functionParamTypes  = functionParamTypes;
            this.functionReturnTypes = functionReturnTypes;
            this.executeExpressions  = executeExpressions;

            this.variableBuilder = toVariableBuilder();
            this.installer = toInstaller();
        }
        VariableBuilder variableBuilder;
        Installer 	    installer;

        public int getOldModifier(){
            return modifier;
        }

        public int getFunctionModifier() {
            return getFunctionModifier(isAnonymous(), modifier);
        }
        public static int getFunctionModifierEmpty() {
            return EMPTY_MODIFIER;
        }
        @SuppressWarnings("ConstantConditions")
        public static int getFunctionModifier(boolean isAnonymous, int oldModifier) {
            if (isAnonymous) {
                return (oldModifier & ANONYMOUS_FUNCTION_SET_FUNCTION_MODIFIER);
            } else {
                return (oldModifier & FUNCTION_SET_FUNCTION_MODIFIER);
            }
        }



        public int getVariableModifier() {
            return getVariableModifier(isAnonymous(), modifier);
        }
        public static int getVariableModifier(boolean isAnonymous, int oldModifier) {
            if (isAnonymous) {
                return EMPTY_MODIFIER; //null
            } else {
                int newMod = (oldModifier & FUNCTION_SET_VAR_MODIFIER);
                if (!isFunctionRemovable(oldModifier)) {
                    newMod |= UNSET;
                }
                return newMod;
            }
        }





        public boolean isAnonymous() {
            return null == functionName;
        }
        public boolean hasName()  { return null != functionName; }
        public boolean hasTypes() { return null != functionParamTypes || null != functionReturnTypes; }


        /**
         * toBuilder 和 toInstaller 必须同时使用 两个生成的参数必须相同
         */
        private VariableBuilder toVariableBuilder() {
            if (isAnonymous()) {
                return new VariableBuilder(this) {
                    @Override
                    public Re_Variable buildBuilder(Re_Executor executor) {
                        // TODO: Implement this method
                        return null;
                    }
                };
            } else {
                final int variableModifier = getVariableModifier();
                boolean hasConst = !isVariableRemovable(variableModifier);

                if (hasConst) {
                    return new VariableBuilder(this) {
                        @Override
                        public Re_Variable buildBuilder(Re_Executor executor) {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_Final_TypeRequired(variableModifier, TYPECHECKER_FUNCTION_TYPE_CHECK);
                        }
                    };
                } else {
                    return new VariableBuilder(this) {
                        @Override
                        public Re_Variable buildBuilder(Re_Executor executor) {
                            // TODO: Implement this method
                            return Re_Variable._newModifierVariable_(variableModifier);
                        }
                    };
                }
            }
        }
        @SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions"})
        private Installer toInstaller() {
            final int functionModifier = getFunctionModifier();
            final boolean isAnonymous = isAnonymous();

            boolean isStruct = isStruct(functionModifier);
            boolean onClassAtInitialization = isStruct;

            if (isAnonymous) {
                if (onClassAtInitialization) {
                    throw new UnsupportedOperationException();
                    //Access Modifier
                } else {
                    return new Installer(this) {
                        @Override
                        protected Object set(Re_CodeLoader.ConvertExpressionAsCallFunction call, Re_Executor executor,
                                             @Nullable String name) {
                            // TODO: Implement this method
                            return createFunction(executor, call.getLine());
                        }
                    };
                }
            } else {
                if (onClassAtInitialization) {
                    if (isStruct) {
                        return new Installer(this) {
                            @Override
                            protected Object set(Re_CodeLoader.ConvertExpressionAsCallFunction current, Re_Executor executor,
                                                 final String name) {
                                int line = current.getLine();
                                // TODO: Implement this method
                                final Re_Class reClass = executor.getReClass();
                                if (null == reClass) {
                                    executor.setThrow(Re_Accidents.executor_no_bind_class());
                                    return null;
                                }
                                if (reClass.isInitialing()) {
                                    //static
                                    Re_Variable variable = superz.variableBuilder.buildBuilder(executor);
                                    if (executor.isReturnOrThrow()) return null;

                                    //put var
                                    Re_Variable.accessPutNewVariableRequire(executor,
                                            name, variable,
                                            reClass);
                                    if (executor.isReturnOrThrow()) return null;

                                    Object value = createFunction(executor, line);
                                    if (executor.isReturnOrThrow()) return null;

                                    Re_Variable.accessSetValue(executor, variable, name, value);
                                    if (executor.isReturnOrThrow()) return null;

                                    //addToList
                                    //类初始化时 将表达式添加到实例初始化表达式列表
                                    /*safe*/reClass.addInstanceInitializationStatementExpression(Re_Variable.createDynamicCallAsExpression(line, name,
                                            /*safe*/new Re_Variable.Re_Variable_DynamicCall.DynamicCall() {
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
                                    return value;
                                } else {
                                    executor.setThrow(Re_Accidents.reclass_initialized(reClass));
                                    return null;
                                }
                            }
                        };
                    } else {
                        throw new UnsupportedOperationException();
                        //Access Modifier
                    }
                } else {
                    return new Installer(this) {
                        @Override
                        protected Object set(Re_CodeLoader.ConvertExpressionAsCallFunction call, Re_Executor executor,
                                             String name) {
                            // TODO: Implement this method
                            Object value = createFunction(executor, call.getLine());
                            if (executor.isReturnOrThrow()) return null;

                            Re_Variable variable = superz.variableBuilder.buildBuilder(executor);
                            if (executor.isReturnOrThrow()) return null;

                            Re_Variable.accessPutNewVariableRequire(executor,
                                    name, variable, executor);
                            if (executor.isReturnOrThrow()) return null;

                            Re_Variable.accessSetValue(executor, variable, name, value);
                            if (executor.isReturnOrThrow()) return null;
                            return value;
                        }
                    };
                }
            }
        }

        protected static abstract class VariableBuilder {
            final MyFunction superz;
            VariableBuilder(MyFunction superz) {
                this.superz = superz;
            }

            protected abstract Re_Variable buildBuilder(Re_Executor executor);
        }



        protected static abstract class Installer {
            final MyFunction superz;
            Installer(MyFunction superz) {
                this.superz = superz;
            }

            protected abstract Object set(Re_CodeLoader.ConvertExpressionAsCallFunction call, Re_Executor executor,
                                          String name);

            protected Re_ClassFunction createFunction(Re_Executor executor,
                                                      int line) {
                return Re_ClassFunction.RuntimeUtils.createReFunction(
                        line,
                        executor,
                        superz.getFunctionModifier(),
                        superz.functionName,
                        superz.functionParamNames, superz.functionParamTypes,
                        superz.functionReturnTypes,
                        superz.getExpression()
                );
            }
        }
    }
}
