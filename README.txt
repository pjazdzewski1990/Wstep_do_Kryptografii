Patryk Ja¿d¿ewski 186507, Informatyka
Podstawy kryptografii, grupa 1

TECHNOLOGIA:			Java, Java 1.7 Oracle 
PLATFORAM:			Windows 7 64-bit
IDE:				NetBeans 7.1
UWAGI:				
					Program stworzono z myœl¹ o przyrostowym rozwijaniu i wzbogacaniu o kolejne algorytmy.
					Sterowanie programem odbywa siê w ca³oœci linii poleceñ.
					Program uruchamiamy np. java -jar Krypto.jar c e a=1 b=2.
					Za³o¿ono, ¿e program bêdzie umiejêtnie obs³ugiwany.
					W przypadku braku argumentów przekazanych z linii poleceñ program skorzysta z domyœlnych
					wartoœci.
					Polecenia podajemy jako pojedyncze ma³e litery, bez myœlnika np. d oznacza dekodowanie.
					Argumenty podajemy w formie nazwa=wartoœæ np. a=2.
					W przypadku wiêkszej iloœci poleceñ lub argumentów odnosz¹cych siê do tego samego pod
					uwagê brany jest jedynie ostatni.
					Jeœli argumenty podane do szyfru afinicznego(a,b) s¹ niepoprawne u¿ytkownik zostanie o tym 
					poinformowany, ale program bedzie kontynuuowa³ operacjê.
					Program wykorzystuje w¹tki do pisania treœci plików, wiêc zdarzyæ siê mo¿e, ¿e g³ówny
					program zakoñczy siê przed wykonaniem siê w¹tku. 
					Przed otworzeniem pliku nale¿y poczekaæ na stosowny komunikat. 
					Szyfr korzysta z pierwszych 127 znaków kodu ASCII, co mo¿e skutkowaæ nietypowymi znakami
					w zaszyfrowanym pliku.
					Program czyta pliki .txt - nie testowano na innych.
					Kryptoanliza z plikiem zaklada, ¿e podany zostanie plik zawieraj¹cy kilka pocz¹tkowych 
					liter z pliki Ÿród³owego (przed jego zakodowaniem).
					Program dopisuje, nie zastêpuje, treœæ do plików. 
ARGUMENTY:
nazwa		wartoœæ domyœlna znaczenie					
a=			2				przesuniêcie kodu afinicznego, tj. ax+b
b=			1				przesuniêcie kodu afinicznego i Cezara
timesDes=	8				ile rund ma wykonaæ algorytm mini DES
debug		nie				czy logowaæ fazy dzia³ania algorytmu
c			tak				kod cezara
a			nie				kod afiniczny
v			nie				szyfr Vinegere'a
x			nie				mini DES
e			tak				szyfrowanie
d			nie				odszyfrowywanie
j			nie				kryptoanaliza z tekstem jawnym
k			nie				kryptoanaliza baz tekstu jawnego
plainFile		plain.txt			plik do szyfrowania
cryptoFile		crypto.txt			plik z zaszyfrowan¹ treœci¹
decryptFile		decrypt.txt			plik to od szyfrowaniu
keyFile			key.txt				plik z kluczem
extraFile		extra.txt			plik stosowany przy kryptoanalizie
analyzeFile		analyze.txt			w pliku znajd¹ siê informacje o krokach 
									wykonywanych przez dany algorytm
					
Zadanie 1
Zrealizowano wszystkie podpunkty. 
Na razie program nie obs³uguje pliku z kluczem (bêdzie to porawione w nastêpnych wersjach).

Przyk³ady:
szyfrujemy szyfrem cezara z przesuniêciem równym 2
java -jar Krypto.jar c e b=2
deszyfrujemy kod afiniczny z a=1 b=2
java -jar Krypto.jar a d a=1 b=2

Zadanie 2
Program potrafi bezb³êdnie zaszyfrowaæ i rozszyfrowaæ dowolny tekst.
W przypadku kryptoanalizy dla bardziej skomplikowanych tekstów program "gubi siê" 
i poprawnie t³umaczy tylko fragmenty tekstu. 

Zadanie 3
Wykonano wszystkie 3 podzadania.
By uruchomiæ u¿ywamy: java -jar Krypto.jar d x debug=true timesDES=8
Jeœli chcemy otrzymaæ analizê poszczegolnych kroków algorytmu musimy 
podaæ opcjê "debug=true".
Wyrazy tekstu do przeksztalcenia (w jedn¹ czy drug¹ stronê) musz¹ byæ oddzielone 
spacjami- domyslnie tekst wynikowy szyfrowania oddzielony jest znakami nowej linii.