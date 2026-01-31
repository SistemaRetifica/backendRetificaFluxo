@echo off
tree /F
echo. >> saida.txt
setlocal
enabledelayedexpansion
for /r . %%d in (.) do (
   echo. >> saida.txt
   echo Diretorio: %%d >> saida.txt
   for %%f in (%%d\*) do (
	   echo Arquivo: %%f >> saida.txt
	   echo Conteudo: >> saida.txt
	   type %%f >> saida.txt 
	   echo. >> saida.txt
   )
)
endlocal
type saida.txt
