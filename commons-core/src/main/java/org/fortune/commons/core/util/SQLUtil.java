package org.fortune.commons.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SQLUtil {

    public static final String EQUAL_OPERATOR = " = ";
    public static final String UNEQUAL_OPERATOR = " != ";
    public static final String IS_OPERATOR = " IS ";
    public static final String IS_NOT_OPERATOR = " IS NOT ";

    public static List<String> queryStringToList(String queryStr) {
        return queryStringToList(queryStr, "|");
    }

    public static List<String> queryStringToList(String queryStr, String delim) {
        List queryList = new ArrayList();
        StringTokenizer stok = new StringTokenizer(queryStr, delim);

        while (stok.hasMoreTokens())
            queryList.add(stok.nextToken().trim());

        return queryList;
    }

    private static boolean canDivide(String queryStr, String delim) {
        return queryStr.indexOf(delim) > -1;
    }

    private static String removeSpace(Object obj) {
        String digit = (String) obj;
        int i = 0;
        while ((i = digit.indexOf(" ")) != -1) {
            digit = digit.substring(0, i) + digit.substring(i + 1);
        }
        return digit;
    }

    public static String getDateFormat(String dateString) {
        int firstSlashPos = dateString.indexOf("/");
        int secondSlashPos = dateString.indexOf("/", firstSlashPos + 1);

        if (secondSlashPos < 0) {
            return "MM/YYYY";
        } else {
            return "MM/DD/YYYY";
        }
    }

    public static String formatDate(String date, String format) {
        return "to_date('" + date + "', '" + format + "')";
    }

    public static String addLetterQuery(String field, String values) {

        return addLetterQuery(field, queryStringToList(values), true);
    }

    public static String assignmentStatus(String param){
        return param.equals("0") ? "N" : "Y";
    }

    private static String addLetterQuery(String field, List values, boolean enableLowerFunction) {
        boolean multiple = values.size() > 1;
        StringBuffer sql = new StringBuffer();
        if (multiple)
            sql.append("(");

        String value = null;
        for (int i = 0; i < values.size(); i++) {
            if (i > 0)
                sql.append("or");

            value = (String) values.get(i);
            value = replaceApostrophe(value);
            if (enableLowerFunction) {
                sql.append(" lower(" + field + ")");
                value = value.toLowerCase();
            } else {
                sql.append(" " + field + " ");
            }

            sql.append(hasWildCard(value) ? " like " : " = ").append("'" + value + "' ");
        }

        if (multiple)
            sql.append(") ");

        return sql.toString();
    }

    private static boolean hasWildCard(String value) {
        return value != null && value.indexOf('%') >= 0;
    }

    public static String replaceApostrophe(String value) {
        if (value == null) {
            return null;
        }

        String apostrophe = "'";
        int index = value.indexOf(apostrophe);

        if (index < 0) {
            return value;
        }

        StringBuffer buf = new StringBuffer();

        int begin = 0;
        int end = index;

        while (end >= 0) {
            if (end > begin) {
                String part = value.substring(begin, end);
                buf.append(part);
                //System.out.println(part);
            }
            buf.append("''");

            begin = end + 1;
            end = value.indexOf(apostrophe, begin);
        }

        String last = value.substring(begin);
        buf.append(last);

        return buf.toString();
    }

    public static String addDigitQuery(String field, String values) {
        if (canDivide(values.toString(), " to "))
            return addDigitRangeQuery(field, queryStringToList(values, " to "), false);

        return addDigitQuery(field, queryStringToList(values), false);
    }

    private static String addDigitRangeQuery(String field, List values, boolean addNotFilter) {
        if (values.size() < 2)
            return "";
        if (addNotFilter){
            return field + " not between " + values.get(0) + " and " + values.get(1);
        }else{
            return field + " between " + values.get(0) + " and " + values.get(1);
        }
    }

    private static String addDigitQuery(String field, List values, boolean addNotFilter) {
        StringBuffer sql = new StringBuffer();
        sql.append(" (");
        for (int i = 0; i < values.size(); i++) {
            if(i>0){
                sql.append(" or ");
            }
            if (getEqualString(values.get(i).toString()).equals(EQUAL_OPERATOR)){
                if(addNotFilter){
                    sql.append(field + UNEQUAL_OPERATOR + removeSpace(values.get(i)));
                }else {
                    sql.append(field + EQUAL_OPERATOR + removeSpace(values.get(i)));
                }
            }
            else{
                if(addNotFilter){
                    sql.append(field + IS_NOT_OPERATOR + values.get(i));
                }else{
                    sql.append(field + IS_OPERATOR + values.get(i));
                }
            }
        }
        sql.append(") ");
        return sql.toString();
    }

    private static String getEqualString(String value){
        if (removeSpace(value).toUpperCase().indexOf("NULL") != -1)
            return IS_OPERATOR;
        else
            return EQUAL_OPERATOR;
    }

    public static String addDateQuery(String field, String values) {
        return addDateQuery(field, queryStringToList(values, " to "), 1);
    }

    private static String addDateQuery(String field, List values, int days) {
        String format = getDateFormat((String) values.get(0));

        String begin = formatDate((String) values.get(0), format);
        String end = values.size() == 1 ? begin : formatDate((String) values.get(1), format);
        if (format.equals("MM/YYYY")) {
            end = "add_months(" + end + ", " + days + ")";
        }
        else {
            end = end + "+" + days;
        }

        return field + ">=" + begin + " and " + field + "<" + end;
    }

    public static String addIssueIdQuery(String field, String issueId){
        String sql = "";

        if (!StringUtils.isEmpty(issueId)) {
            String[] ids = issueId.split("\\|");
            for (int i=0; i<ids.length; i++) {
                String curIssue = ids[i].trim();
                if (!StringUtils.isEmpty(curIssue)) {
                    sql += " OR UPPER(" + field + ") LIKE '%" + curIssue.toUpperCase()
                            + "' OR UPPER(" + field + ") LIKE '%" + curIssue.toUpperCase() + ";%' ";
                }
            }

            if (!StringUtils.isEmpty(sql)) {
                sql = " (" + sql.substring(3) + ") ";
            }
        }

        return sql;
    }

}
