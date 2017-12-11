@ECHO OFF
set app=erachain-dev
set xms=128
set xmx=256
set mms=128

IF EXIST java (
	start "%app%" java -Xms%xms%m -Xmx%xmx%m -XX:MaxMetaspaceSize=%mms%m -XX:MaxMetaspaceSize=%mms%m -jar %app%.jar -pass=1
	rem EXIT /b
)

REG QUERY "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\1.7" /v "JavaHome" >nul 2>nul || ( GOTO NOTFOUND1 )
	for /f "tokens=1,2,*" %%a in ('reg query "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\1.7" /v "JavaHome"') do if "%%a"=="JavaHome" set JAVAHOME=%%c

IF EXIST "%JAVAHOME%\bin\java.exe" (
	start "%app%" "%JAVAHOME%\bin\java.exe" -Xms%xms%m -Xmx%xmx%m -XX:MaxMetaspaceSize=%mms%m -jar %app%.jar -pass=1
	EXIT /b
)

:NOTFOUND1

REG QUERY "HKLM\SOFTWARE\WOW6432NODE\JavaSoft\Java Runtime Environment\1.7" /v "JavaHome" >nul 2>nul || ( GOTO NOTFOUND2 )
	for /f "tokens=1,2,*" %%a in ('reg query "HKLM\SOFTWARE\WOW6432NODE\JavaSoft\Java Runtime Environment\1.7" /v "JavaHome"') do if "%%a"=="JavaHome" set JAVAHOME=%%c

IF EXIST "%JAVAHOME%\bin\java.exe" (
	start "%app%" "%JAVAHOME%\bin\java.exe" -Xms%xms%m -Xmx%xmx%m -XX:MaxMetaspaceSize=%mms%m -jar %app%.jar -pass=1
	EXIT /b
)

:NOTFOUND2

REG QUERY "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\1.8" /v "JavaHome" >nul 2>nul || ( GOTO NOTFOUND3 )
	for /f "tokens=1,2,*" %%a in ('reg query "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\1.8" /v "JavaHome"') do if "%%a"=="JavaHome" set JAVAHOME=%%c
	
IF EXIST "%JAVAHOME%\bin\java.exe" (
	start "%app%" "%JAVAHOME%\bin\java.exe" -Xms%xms%m -Xmx%xmx%m -XX:MaxMetaspaceSize=%mms%m -jar %app%.jar -pass=1
	EXIT /b
)

:NOTFOUND3

REG QUERY "HKLM\SOFTWARE\WOW6432NODE\JavaSoft\Java Runtime Environment\1.8" /v "JavaHome" >nul 2>nul || ( GOTO NOTFOUND4 )
	for /f "tokens=1,2,*" %%a in ('reg query "HKLM\SOFTWARE\WOW6432NODE\JavaSoft\Java Runtime Environment\1.8" /v "JavaHome"') do if "%%a"=="JavaHome" set JAVAHOME=%%c

IF EXIST "%JAVAHOME%\bin\java.exe" (
	start "%app%" "%JAVAHOME%\bin\java.exe" -Xms%xms%m -Xmx%xmx%m -XX:MaxMetaspaceSize=%mms%m -jar %app%.jar -pass=1
	EXIT /b
)
	
:NOTFOUND4

ECHO Java software not found on your system. Please go to http://java.com to download a copy of Java.
PAUSE