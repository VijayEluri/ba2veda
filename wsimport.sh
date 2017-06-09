# ----------
# UTF-8
# Скрипт отвечает за генерацию файлов клиента по wsdl
# Протестировано на Ubuntu 10.04; JDK 1.6.0_20
# ----------
wsimport -verbose -s ./src/main/java/ -keep -d ./target/classes/ -p com.bigarchive.filemanager ./wsdl/filemanager.wsdl
