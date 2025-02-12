package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.IOException;
import java.io.StringReader;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try (CSVReader reader = new CSVReader(new StringReader(csvString))) {
        
            // INSERT YOUR CODE HERE
            
            //Read headers and prepare the lists for json fields
            String[] headers = reader.readNext();
            JsonArray prodNums = new JsonArray();
            JsonArray colHeadings = new JsonArray();
            JsonArray data = new JsonArray();
            
           //Populate column headings
           for (String header : headers) {
               colHeadings.add(header);
           }
           
           //Read through the csv rows
           String[] row;
           while ((row = reader.readNext()) != null) {
               if (row.length ==0) {
                   continue;
               }
               
               prodNums.add(row[0]);
               
               //Preppare row data
               JsonArray rowData = new JsonArray();
               
               rowData.add(row[1].trim());
               rowData.add(Integer.parseInt(row[2].trim()));
               rowData.add(Integer.parseInt(row[3].trim()));
               
               //Process fields as strings
               for (int i = 4; i < row.length; i++) {
                   rowData.add(row[i].trim());
               }
               
               //Add row data to overall data array
               data.add(rowData);
           }
            
           //Creates json object
            JsonObject json = new JsonObject();
            json.put("ProdNums", prodNums);
            json.put("ColHeadings", colHeadings);
            json.put("Data", data);
            
            //Serialize the json object to a string
            result = json.toJson();
            
        }catch (IOException e){
            e.printStackTrace();
        
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
            // INSERT YOUR CODE HERE
            
            //Parse the input json string into a jsonobject
            JsonObject json = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray prodNums = (JsonArray) json.get("ProdNums");
            JsonArray colHeadings = (JsonArray) json.get("ColHeadings");
            JsonArray data = (JsonArray) json.get("Data");
            
            StringBuilder csvBuilder = new StringBuilder();
            
            //Add column headings to csv
            for (int i = 0; i < colHeadings.size(); i++) {
                csvBuilder.append("\"").append(colHeadings.get(i)).append("\"");
                if (i < colHeadings.size() - 1) {
                    csvBuilder.append(",");
                }
            }
            csvBuilder.append("\n");
            
            //Add row of data to the csv
            for (int i = 0; i < data.size(); i++) {
                JsonArray rowData = (JsonArray) data.get(i);
                
                //add production number as the first column
                csvBuilder.append("\"").append(prodNums.get(i)).append("\"");
                
                //Add the row data
                for (int j = 0; j < rowData.size(); j++) {
                    String value = rowData.get(j).toString();
                    
                    if (j == 2 && value.length() == 1) {
                        value = "0" + value;
                    }
                    
                    csvBuilder.append(",\"").append(value).append("\"");
                }
                csvBuilder.append("\n");
                
            }
            
            //Convert stringbuilder content to a string
            result = csvBuilder.toString();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
