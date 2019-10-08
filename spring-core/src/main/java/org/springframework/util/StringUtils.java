package org.springframework.util;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 字符串工具类
 */
public abstract class StringUtils {

    /**
     * linux路径分隔符
     */
    public static final String FLODER_SEPARATOR = "/";

    /**
     * windows路径分隔符
     */
    public static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";
    /**
     * 简化url路径
     * @param path 路径
     * @return
     */
    public static String cleanPath(String path){
        if(!hasLength(path)){
            return path;
        }

        //讲window路径转为linux路径
        String pathToUse = replace(path,WINDOWS_FOLDER_SEPARATOR,FLODER_SEPARATOR);

        //如果没有相对路径符，直接返回
        if(pathToUse.indexOf(".") == -1){
            return pathToUse;
        }

        int preIndex = pathToUse.indexOf(":");
        String prefix = "";
        if(preIndex != -1){
            prefix = pathToUse.substring(0,preIndex + 1);

            if(prefix.contains(FLODER_SEPARATOR)){
                prefix = "";
            }else{
                pathToUse = pathToUse.substring(preIndex + 1);
            }
        }

        if(pathToUse.startsWith(FLODER_SEPARATOR)){
            prefix += FLODER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        //获取路径中的目录
        String[] pathArray = delimitedListToStringArray(pathToUse,FLODER_SEPARATOR);
        List<String> pathElement = new ArrayList<>();
        int top = 0;
        for(int i=pathArray.length - 1;i >=0 ;i--){
            String tempPath = pathArray[i];

            if(CURRENT_PATH.equals(tempPath)){

            }else if(TOP_PATH.equals(tempPath)){
                top ++;
            }else{
                if(top >0 ){
                    top --;
                }else{
                    pathElement.add(0,tempPath);
                }
            }
        }

        for(int i=0;i<top;i++){
            pathElement.add(0,TOP_PATH);
        }

        if(pathElement.size() == 1 &&pathElement.get(0).equals("")&& !prefix.endsWith(FLODER_SEPARATOR)){
            pathElement.add(0,".");
        }


        return prefix + collectionToDelimitedString(pathElement,FLODER_SEPARATOR);
    }

    /**
     * 替换字符串
     * @param inString 输入字符串
     * @param oldPattern 被替换的字符串
     * @param newPattern 替换成的目标字符串
     * @return
     */
    public static String replace(String inString,String oldPattern,@Nullable String newPattern){
        if(!hasLength(inString) || !hasLength(oldPattern) || newPattern == null){
            return inString;
        }

        int index = inString.indexOf(oldPattern);
        if(index == -1){
            return inString;
        }

        int capacity = inString.length();
        if(newPattern.length() > oldPattern.length()){
            capacity += 16;
        }

        StringBuilder sb = new StringBuilder(capacity);
        int patLen = oldPattern.length();
        int pos = 0;
        while(index >= 0){
            sb.append(inString.substring(pos,index));
            sb.append(newPattern);
            pos = index +patLen;
            index = inString.indexOf(oldPattern,pos);
        }

        sb.append(inString.substring(pos));
        return sb.toString();
    }

    /**
     * 将集合转为字符串
     * @param collection 集合
     * @param delimitor 分隔符
     * @return
     */
    public static String collectionToDelimitedString(Collection<?> collection,String delimitor){
        return collectionToDelimitedString(collection,delimitor,"","");
    }


    /**
     * 将集合转为字符串
     * @param collection 集合
     * @param delimitor 分隔符
     * @param prefix 前缀
     * @param suffix 后缀
     * @return
     */
    public static String collectionToDelimitedString(Collection<?> collection,String delimitor,String prefix,String suffix){
        if(CollectionUtils.isEmpty(collection)){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        while(it.hasNext()){
            String temp = prefix + it.next() + suffix;
            if(it.hasNext())
                temp += delimitor;
            sb.append(temp);
        }
        return sb.toString();
    }


    /**
     * 删除字符串中指定字符串，将剩余字符串分割
     * @param inString 输入字符串
     * @param delimitor 分割字符串
     * @return
     */
    public static String[] delimitedListToStringArray(@Nullable  String inString,@Nullable String delimitor){
        return delimitedListToStringArray(inString,delimitor,null);
    }


    /**
     * 删除字符串中指定字符串，将剩余字符串分割
     * @param inString 输入字符串
     * @param delimitor 分割字符串
     * @param charsToDelete 需要删除的字符
     * @return
     */
    public static String[] delimitedListToStringArray(@Nullable  String inString,@Nullable String delimitor,@Nullable String charsToDelete){
        if(inString == null){
            return EMPTY_STRING_ARRAY;
        }

        if(delimitor == null){
            return new String[]{inString};
        }

        List<String> result = new ArrayList<>();
        if(delimitor.equals("")){
            for(int i=0;i<inString.length();i++){
                result.add(deleteAny(inString.substring(i,i+1),charsToDelete));
            }
        }else{
            // inString:“33454512435435124535”
            //delimitor : "12"
            int delPos = 0;
            int pos = 0;
            while((delPos = inString.indexOf(delimitor,delPos))!= -1){
                result.add(deleteAny(inString.substring(pos,delPos),charsToDelete));
                delPos += delimitor.length();
                pos = delPos;
            }

            //将剩余的字符串加入
            if(inString.length() > 0 && pos <= inString.length()){
                result.add(deleteAny(inString.substring(pos),charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * 将字符串列表转为字符串数组
     * @param collection
     * @return
     */
    public static String[] toStringArray(Collection<String> collection){
        return !CollectionUtils.isEmpty(collection)?collection.toArray(EMPTY_STRING_ARRAY):EMPTY_STRING_ARRAY;
    }

    /**
     * 删除字符串中指定的字符串
     * @param inString 被删除的字符串对象
     * @param charsToDelete 需要删除的字符
     * @return
     */
    public static String deleteAny(String inString,String charsToDelete){
        if(!hasLength(inString) || !hasLength(charsToDelete)){
            return inString;
        }
        StringBuilder sb  = new StringBuilder();
        for(int i=0;i<inString.length();i++){
            char c = inString.charAt(i);
            if(charsToDelete.indexOf(c) == -1){
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否有长度 "" or null
     * @param str
     * @return
     */
    public static boolean hasLength(String str){
        return str != null && !"".equals(str);
    }


    /**
     * 获取文件名
     * @param path 路径
     * @return
     */
    @Nullable
    public static String getFilename(String path){
        if(path == null){
            return null;
        }

        int separator = path.lastIndexOf(FLODER_SEPARATOR);
        return separator != - 1?path.substring(separator + 1):path;
    }

    /**
     * 根据路径和相对路径创建一个绝对路径
     * @param path 路径
     * @param relativePath 相对路径
     * @return
     */
    public static String applyRelativePath(String path,String relativePath){
        int separator = path.lastIndexOf(FLODER_SEPARATOR);
        if(separator != -1){
            String newPath = path.substring(0,separator);
            if(!relativePath.startsWith(FLODER_SEPARATOR)){
                newPath += FLODER_SEPARATOR;
            }
            return newPath + relativePath;
        }else{
            return relativePath;
        }
    }
}
