:: -------------------------------------------------------------------
::
:: Courbes
:: Copyright 2015-16 L3 Info UAPV 2015-16
:: 
:: This file is part of Courbes.
:: 
:: Courbes is free software: you can redistribute it and/or modify it under the terms 
:: of the GNU General Public License as published by the Free Software Foundation, 
:: either version 2 of the License, or (at your option) any later version.
:: 
:: Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
:: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
:: PURPOSE. See the GNU General Public License for more details.
:: 
:: You should have received a copy of the GNU General Public License
:: along with Courbes. If not, see <http://www.gnu.org/licenses/>.
:: 
:: -------------------------------------------------------------------

	Setlocal

:: define path variables
	Set bin=.\bin
	Set kryo=.\lib\kryonet-2.21-all.jar
	Set cp=%bin%;%kryo%
	Set launcher=fr.univavignon.courbes.Launcher

:: launch the game
	java -Xmx512m -classpath %cp% %launcher%

:: 	pause

	Endlocal
