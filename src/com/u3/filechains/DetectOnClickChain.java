package com.u3.filechains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectOnClickChain extends BaseChain {
    private static final int HAVE_ARG = 2;
    private static final int NO_ARG = 1;
    private Map<ClickMehtod, List<String>> methodAndIDMap;
    private ClickMehtod method;
    private List<String> ids;

    public DetectOnClickChain(Map<ClickMehtod, List<String>> map){
        methodAndIDMap = map;
    }
    @Override
    public void process() {
        String pattern = "^@OnClick\\(\\{*(R.id.*,|R.id.*|R2.id.*|R2.id.*,)+\\}*\\)$";
        Pattern r = Pattern.compile(pattern);
        for (int i = 0;i < currentDoc.length;i++){
            Matcher m = r.matcher(currentDoc[i].trim());
            currentDoc[i] = currentDoc[i].trim();
            if (m.find()) {
                method = detectMethod(currentDoc[i+1]);
                ids = detectID(currentDoc[i], method);
                methodAndIDMap.put(method,ids);
                deleteLineNumbers.add(i);
            }
        }
    }

    private List<String> detectID(String s, ClickMehtod mehtod) {
        List<String> idList;
        if (s.contains("{")){
            int curlyLeftIndex = s.indexOf("{");
            int curlyRightIndex = s.indexOf("}");
            String[] ids = s.substring(curlyLeftIndex+1,curlyRightIndex).split(",");
            idList = Arrays.asList(ids);
        }else{
            String id = s.substring(s.indexOf("(")+1, s.length()-1);
            idList = Arrays.asList(id);
        }
        return idList;
    }

    private ClickMehtod detectMethod(String s) {
        ClickMehtod method = new ClickMehtod();
        String[] elements = s.split(" ");
        for(String element:elements){
            if (element.contains("(")){
                setMethod(element,method);
                break;
            }
        }
        return method;
    }

    private void setMethod(String element,ClickMehtod method) {
        String[] nameAndArg = element.split("\\(");
        if (nameAndArg.length == HAVE_ARG&&!nameAndArg[1].equals(")")){
            method.setName(nameAndArg[0]);
            method.setArgType(nameAndArg[1]);
            method.setHaveArg(true);
        }else{
            method.setName(nameAndArg[0]);
            method.setHaveArg(false);
        }
    }
}
