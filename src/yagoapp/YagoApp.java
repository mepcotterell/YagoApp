package yagoapp;

import com.hp.hpl.jena.rdf.model.RDFNode;
import converters.SPARQLprocessor;
import java.io.FileInputStream;
import java.io.IOException;

import static java.lang.System.in;
import static java.lang.System.out;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author mepcotterell
 */
public class YagoApp {
  
    public static Properties      props  = new Properties();
    public static SPARQLprocessor sparql = null;
    public static Scanner         kb     = new Scanner(in);
    
    public static String ep1 = "";
    public static String ep2 = "";
   
    static final Map<String, String> prefixMap = new HashMap<String, String>() {
        {
            put ("http://yago-knowledge.org/resource/", ":");
            put ("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:");
            put ("http://www.w3.org/2000/01/rdf-schema#", "rdfs:");
        }
    };
    
    /**
     * Sanitizes SPARQL endpoints in order to replace full URI prefixes with
     * short prefixes.
     * @param str
     * @return 
     */
    public static String sanitize (String str) {
        String returnValue = str;
        for (String key : prefixMap.keySet()) {
            if (str.startsWith(key)) {
                returnValue = str.replace(key, prefixMap.get(key));
            } // if
        } // for
        return returnValue;
    } // prefix
    
    /**
     * Returns the format string for a SELECT * query in SPARQL.
     * This naive approach creates the format string from the WHERE clause.
     * @param query
     * @return 
     */
    public static String extractFormatString (String query) {
        String str = query.substring("SELECT DISTINCT * WHERE { ".length());
        str = str.replaceAll("}", "");
        return str;
    } // extractFormatString
    
    /**
     * Prints the results of the SPARQL query.
     * @param query 
     */
    public static void printResults (String query) {
        
        out.println(String.format("Query: %s", query));
        Iterable<Map<String, RDFNode>> result1 = sparql.query(query, 20);
        
        out.println("Results:");
        for (Map<String, RDFNode> row : result1) {
            String rowLine = extractFormatString(query);
            for (String key : row.keySet()) {
                rowLine = rowLine.replaceAll("\\?" + key, sanitize(row.get(key).toString()));
                //rowLine += String.format("(%s = %s)", key, sanitize(row.get(key).toString()));
            } //for
            //rowLine += " " + ep2;
            out.println(rowLine);
        } // for
    } // printResults
    
    public static void main(String[] args) {
        
        try {
            FileInputStream fis = new FileInputStream("src/yagoapp/YagoApp.properties");
            props.load(fis);
            sparql = new SPARQLprocessor(props.getProperty("jenaTDBdirectory"));
        } catch (IOException ex) {
            Logger.getLogger(YagoApp.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } // try
        
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
        
        out.println();
        out.println("Querying for four-hop relationships...");
       
        query = String.format("SELECT DISTINCT * WHERE { %s ?p1 ?o2 . ?o2 ?p2 ?o3 . ?o3 ?p3 ?o4 . ?o4 ?p4 %s . }", ep1, ep2);
        printResults(query);
       
        out.println();
        out.println("Querying for five-hop relationships...");
       
        query = String.format("SELECT DISTINCT * WHERE { %s ?p1 ?o2 . ?o2 ?p2 ?o3 . ?o3 ?p3 ?o4 . ?o4 ?p4 ?o5 . ?o5 ?p5 %s . }", ep1, ep2);
        printResults(query);
    
    } // main
    
} // YagoApp
