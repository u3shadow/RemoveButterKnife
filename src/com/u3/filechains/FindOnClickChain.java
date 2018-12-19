package com.u3.filechains;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindOnClickChain extends BaseChain {
    private static final int HAVE_ARG = 2;
    private static final int NO_ARG = 1;
    private Map<ClickMehtod, List<String>> methodAndIDMap;
    public FindOnClickChain(Map<ClickMehtod, List<String>> map){
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
                ClickMehtod mehtod = getMethod(currentDoc[i+1]);
                if (currentDoc[i].contains("{")){
                    int curlyLeftIndex = currentDoc[i].indexOf("{");
                    int curlyRightIndex = currentDoc[i].indexOf("}");
                    String[] ids = currentDoc[i].substring(curlyLeftIndex+1,curlyRightIndex).split(",");
                    updateMap(mehtod,ids);
                }else{
                  String id = currentDoc[i].substring(currentDoc[i].indexOf("(")+1,currentDoc[i].length()-1);
                  updateMap(mehtod,id);
                }
                deleteLineNumbers.add(i);
            }
        }
    }

    private ClickMehtod getMethod(String s) {
        ClickMehtod method = new ClickMehtod();
        String[] elements = s.split(" ");
        for(String element:elements){
            if (element.contains("(")){
                String[] nameAndArg = element.split("\\(");
                if (nameAndArg.length == HAVE_ARG){
                    method.setName(nameAndArg[0]);
                    method.setArgType(nameAndArg[1]);
                    method.setHaveArg(true);
                }else if (nameAndArg.length == NO_ARG){
                    method.setName(nameAndArg[0]);
                    method.setHaveArg(false);
                }
                break;
            }
        }
        return method;
    }
    private void updateMap(ClickMehtod method,String ...ids){
        List<String> idList = new ArrayList<>();
        for (String s:ids){
            idList.add(s);
        }
        methodAndIDMap.put(method,idList);
    }
}
