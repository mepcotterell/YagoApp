/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yagoapp;

import com.hp.hpl.jena.rdf.model.RDFNode;
import static java.lang.System.in;
import static java.lang.System.out;

import converters.SPARQLprocessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author mepcotterell
 */
public class YagoApp {

    public static final SPARQLprocessor sparql = new SPARQLprocessor("/home/mepcotterell/Desktop/ais/yago2core_jena_20120109");
    public static final Scanner kb = new Scanner(in);
    
    public static String ep1 = "";
    public static String ep2 = "";
   
    static final Map<String, String> prefixMap = new HashMap<String, String>() {
        {
            put ("http://yago-knowledge.org/resource/", "yago:");
        }
    };
    
    public static String sanitize (String str) {
        String returnValue = str;
        for (String key : prefixMap.keySet()) {
            if (str.startsWith(key)) {
                returnValue = str.replace(key, prefixMap.get(key));
            } // if
        } // for
        return returnValue;
    } // prefix
    
    public static void printResults (String query) {
        Iterable<Map<String, RDFNode>> result1 = sparql.query(query, 20);
        
        out.println("Results:");
        for (Map<String, RDFNode> row : result1) {
            String rowLine = "" +  ep1 + " ";
            for (String key : row.keySet()) {
                //rowLine += String.format("%s %s %s", key, sanitize(row.get(key).toString()), sanitize(row.get(key).asResource().getLocalName()));
                //out.println(String.format("%25s %25s %25s", ep1, sanitize(row.get(key).toString()), ep2));
                rowLine += "( " + row.get(key) + ") ds";
            } //for
            rowLine += " " + ep2;
            out.println(rowLine);
        } // for
    } // printResults
    
    public static void main(String[] args) {
        
        String query = "";
        
        out.print("Please enter the first endpoint: ");
        ep1 = kb.nextLine().trim();
       
        out.print("Please enter the second endpoint: ");
        ep2 = kb.nextLine().trim();
       
        out.println();
        out.println("Querying for single hop relationships...");
       
        query = String.format("SELECT DISTINCT * WHERE { %s ?p %s . }", ep1, ep2);
        printResults(query);
        
        out.println();
        out.println("Querying for double hop relationships...");
       
        query = String.format("SELECT DISTINCT * WHERE { %s ?p1 ?o . ?o ?p2 %s . }", ep1, ep2);
        printResults(query);
        
        out.println();
        out.println("Querying for triple hop relationships...");
       
        query = String.format("SELECT DISTINCT * WHERE { %s ?p1 ?o2 . ?o2 ?p2 ?o3 . ?o3 ?p3 %s . }", ep1, ep2);
        printResults(query);
       
    
    } // main
    
} // YagoApp
