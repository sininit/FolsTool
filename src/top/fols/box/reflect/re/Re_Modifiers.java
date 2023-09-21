package top.fols.box.reflect.re;

import top.fols.atri.util.Lists;

import java.util.*;

@SuppressWarnings({"unchecked"})
public class Re_Modifiers {
    //has(mod, UNSET|FINAL|THIS|STATIC)
    public static boolean has(int mod, int modifier) { return (mod & modifier)  != 0; }
    //add(mod, FINAL|UNSET)
    public static int     add(int old, int modifier) { return old | modifier; }
    //del(mod, STATIC|THIS)
    public static int     del(int old, int modifier) {
        int at = old & modifier;
        if (at != 0) {
            old ^= at;
        }
        return old;
    }


    public static final int EMPTY_MODIFIER = 0x00000000;


    public static final int UNSET  = 0x00000001;//代表可重写   但不可修改
    public static final int FINAL  = 0x00000002;//代表不可重写 也不可修改

    public static final int STATIC = 0x00000004;
    public static final int THIS   = 0x00000008;

    public static final int STRUCT = 0x00000010;

    static final String _STR_FINAL  = "final";
    static final String _STR_UNSET  = "unset";
    static final String _STR_STATIC = "static";
    static final String _STR_THIS   = "this";
    static final String _STR_STRUCT = "struct";


    static final String                 MODIFIER_TO_STRING_SEPARATOR = Re_CodeLoader.CODE_BLANK_SPACE_CHARS;
    static final Map<String, Integer>   MODIFIER_NAME_MOD_MAP = new HashMap<String, Integer>() {{
        put(_STR_FINAL,  FINAL);
        put(_STR_UNSET,  UNSET);

        put(_STR_STATIC, STATIC);
        put(_STR_THIS,   THIS);

        put(_STR_STRUCT, STRUCT);
    }};


    public static final ModifierVerifier MODIFIER_GROUP_INIT_MV = new ModifierVerifier(new Set[]{
            Lists.asLinkedHashSet(_STR_FINAL)
    });
    public static final ModifierVerifier MODIFIER_GROUP_VAR_MV = new ModifierVerifier(new Set[]{
            Lists.asLinkedHashSet(_STR_FINAL),
            Lists.asLinkedHashSet(_STR_UNSET),

            Lists.asLinkedHashSet(_STR_STATIC, _STR_THIS, _STR_STRUCT)
    });
    public static final ModifierVerifier MODIFIER_GROUP_FUNCTION_MV = new ModifierVerifier(new Set[] {
            Lists.asLinkedHashSet(_STR_FINAL),

            Lists.asLinkedHashSet(_STR_STRUCT)
    });
    public static final ModifierVerifier MODIFIER_GROUP_FUNCTION_ANONYMOUS_MV = new ModifierVerifier(new Set[] {
    });



    //所有的访问权限符号
    public static final int ACCESS_PERMISSION_MODIFIER = EMPTY_MODIFIER;


    //init 本质上是个 Function 所以它允许的修饰符 必须在 FUNCTION_MODIFIER内
    public static final int INIT_ALL_MODIFIER               = (FINAL);
    //Var 允许的所有修饰符
    public static final int VAR_ALL_MODIFIERS               = (UNSET | FINAL) | (STRUCT | THIS | STATIC);
    //Function 允许的所有修饰符
    public static final int FUNCTION_ALL_MODIFIER 	        = (FINAL) | (STRUCT);
    //匿名Function 允许的所有修饰符
    public static final int ANONYMOUS_FUNCTION_ALL_MODIFIER = (EMPTY_MODIFIER);




    //Var 不允许删除的修饰符
    static final int VAR_UNMOVABLE_MODIFIER                     = (UNSET | FINAL) | (STRUCT | THIS | STATIC);
    //Function 不允许删除的修饰符
    static final int FUNCTION_UNMOVABLE_MODIFIER                = (FINAL) | (STRUCT);

    //Function 设置Var时允许的修饰符
    static final int FUNCTION_SET_VAR_MODIFIER                  = (FINAL) | (STRUCT);
    //Function 创建Function时允许的修饰符
    static final int FUNCTION_SET_FUNCTION_MODIFIER             = Re_Modifiers.FUNCTION_ALL_MODIFIER;

    static final int ANONYMOUS_FUNCTION_SET_FUNCTION_MODIFIER   = Re_Modifiers.ANONYMOUS_FUNCTION_ALL_MODIFIER;



    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isVariableRemovable(int oldModifier) {
        return (oldModifier & VAR_UNMOVABLE_MODIFIER) == 0;
    }
    public static boolean isFunctionRemovable(int oldModifier) {
        return (oldModifier & FUNCTION_UNMOVABLE_MODIFIER) == 0;
    }



    public static int buildModifier(boolean isFinal,  boolean isUnset,
                                    boolean isStatic, boolean isThis,

                                    boolean isStruct
    ) {
        int mod = 0;
        if (isFinal)   mod |= FINAL;
        if (isUnset)   mod |= UNSET;

        if (isStatic)  mod |= STATIC;
        if (isThis)    mod |= THIS;

        if (isStruct)  mod |= STRUCT;
        return mod;
    }


    public static boolean isFinal(int mod)   { return (mod & FINAL)     != 0; }
    public static boolean isUnset(int mod)   { return (mod & UNSET)     != 0; }

    public static boolean isStatic(int mod)  { return (mod & STATIC)    != 0; }
    public static boolean isThis(int mod)    { return (mod & THIS)      != 0; }

    public static boolean isStruct(int modifier) { return (modifier & STRUCT) != 0; }




    public static boolean isModifier(String str) {
        return null != MODIFIER_NAME_MOD_MAP.get(str);
    }
    public static int parseModifier(Set<String> modifiers) {
        int mod = 0;
        for (String modifier: modifiers) {
            Integer magic = MODIFIER_NAME_MOD_MAP.get(modifier);
            if (null != magic) {
                if ((mod &  magic) == 0) {
                     mod |= magic;
                }
            }
        }
        return mod;
    }
    public static String asString(int... modifier) {
        int mod = 0;
        for (int i : modifier) {
            mod |= i;
        }
        return asString(mod);
    }
    public static String asString(int mod) {
        StringBuilder sb = new StringBuilder();
        int len;

        for (String name : MODIFIER_NAME_MOD_MAP.keySet()) {
            int MAGIC = MODIFIER_NAME_MOD_MAP.get(name);

            if ((mod & MAGIC) != 0) sb.append(name).append(MODIFIER_TO_STRING_SEPARATOR);
        }

        if ((len = sb.length()) > 0)    /* trim trailing space */
            return sb.substring(0, len - MODIFIER_TO_STRING_SEPARATOR.length());
        return "";
    }

    public static final ModifierVerifier INIT_MV                = MODIFIER_GROUP_INIT_MV;
    public static final ModifierVerifier VAR_MV                 = MODIFIER_GROUP_VAR_MV;
    public static final ModifierVerifier FUNCTION_MV            = MODIFIER_GROUP_FUNCTION_MV;
    public static final ModifierVerifier FUNCTION_ANONYMOUS_MV  = MODIFIER_GROUP_FUNCTION_ANONYMOUS_MV;











    public static class ModifierVerifier {
        Map<String, Integer> modifierList;
        Set<String>[]        modifierGroup;

        public ModifierVerifier(Set<String>[]        modifierGroup) {
            this.modifierGroup = modifierGroup;
            this.modifierList  = new HashMap<>();
            for (Set<String> names : modifierGroup) {
                for (String name : names) {
                    Integer value = MODIFIER_NAME_MOD_MAP.get(name);
                    if (null == value){
                        throw new UnsupportedOperationException("get mod err: " + name);//理论上不可能发生
                    }
                    modifierList.put(name, value);
                }
            }
        }

        public String testModifier(Set<String> modifiers) {
            for (String m: modifiers) {
                Integer magic = modifierList.get(m);
                if (null == magic) {
                    return "unknown modifier: " + m;
                }
            }
            for (Set<String> ele: modifierGroup) {
                int magic = 0;
                for (String e: ele) {
                    int m = modifierList.get(e);
                    if (modifiers.contains(e)) {
                        if (magic != 0) {
                            return String.format("simultaneous occurrences are not allowed: [%s, %s]", Re_Modifiers.asString(magic) , e);
                        } else {
                            magic |= m;
                        }
                    }
                }
            }
            return null;
        }
    }

    static class _______{}


}
