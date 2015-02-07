package src.main.bluepoet

import groovy.sql.Sql
import groovy.xml.MarkupBuilder;
import org.apache.commons.lang3.StringUtils;

def environment_properties = "db_oracle.properties"
def props = new Properties()

new File(environment_properties).withInputStream { stream ->
	props.load(stream)
}

sql = Sql.newInstance(props["db.url"],props["db.username"],props["db.password"],props["db.driver"])

def writer = new StringWriter();
def xml = new MarkupBuilder(writer);
def tableName = args[0]
def column
def columnsList = []

sql.eachRow('select column_name from user_tab_columns where table_name=? and nullable = \'N\' ORDER BY COLUMN_ID', [tableName]) {
    columnsList.add(it.COLUMN_NAME)
}

if(columnsList.size() == 0) {
    println "테이블 혹은 테이블안에 데이터가 없습니다. 확인하세요"
}else{
	 def attributes =''

     xml.mkp.xmlDeclaration(version: "1.0", encoding: "UTF-8")
     xml.dataset {
		 sql.eachRow('select * from '+tableName+' WHERE ROWNUM <= 1') { row ->
			 for(int i=0; i<columnsList.size(); i++) {
			     def col = columnsList[i]
					 attributes += col+'=\"'+row[col]+'\"'
                    	 if(i != columnsList.size()-1) {
							 attributes += ' '
                         } 
             }

             "${tableName}"(attributes) 
            
			 attributes = ''
        }
    }

	def result = writer.toString()
	def firstReplacePos = result.indexOf(tableName) + tableName.size()
	
	result = StringUtils.replace(result, '\'', '\"')
	result = StringUtils.replace(result, '<'+tableName+'>', '<'+tableName+' ')
	result = StringUtils.replace(result, '</'+tableName+'>', ' />')
	
	writeToFile(tableName + '.xml', result)
	
	println "xml이 정상적으로 생성되었습니다^^"
} 

sleep(1500)

public void writeToFile(def fileName, def contents){
    new File("$fileName").withWriter("UTF-8", { out ->
        contents.each {
            out.print it
        }
    })
}