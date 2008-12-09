set path=%path%;E:\j2sdk1.4.2_08\bin
echo java -classpath ..\lib\jdbc\hsql\hsqldb.jar org.hsqldb.util.%1 %2 %3 %4 %5 %6 %7 %8 %9
java -cp ..\lib\jdbc\hsql\hsqldb.jar org.hsqldb.Server -database.0 db/mydb -dbname.0 xdb 
