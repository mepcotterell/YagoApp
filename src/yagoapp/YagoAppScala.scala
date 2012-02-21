package yagoapp

import com.hp.hpl.jena.rdf.model.RDFNode
import converters.SPARQLprocessor
import java.io.{FileInputStream, IOException}

object YagoAppScala extends App {

  val props     = new Properties()
  val kb        = new Scanner(java.lang.System.in)

  val prefixMap = (
    "http://yago-knowledge.org/resource/"         => ":",
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#" => "rdf:",
    "http://www.w3.org/2000/01/rdf-schema#"       => "rdfs:"
  ) // prefixMap

  

} // YagoAppScala
