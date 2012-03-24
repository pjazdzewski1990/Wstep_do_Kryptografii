Patryk Ja�d�ewski 186507, Informatyka
Podstawy kryptografii, grupa 1

TECHNOLOGIA:			Java, Java 1.7 Oracle 
PLATFORAM:			Windows 7 64-bit
IDE:				NetBeans 7.1
UWAGI:				
					Program stworzono z my�l� o przyrostowym rozwijaniu i wzbogacaniu o kolejne algorytmy.
					Sterowanie programem odbywa si� w ca�o�ci linii polece�.
					Program uruchamiamy np. java -jar Krypto.jar c e a=1 b=2.
					Za�o�ono, �e program b�dzie umiej�tnie obs�ugiwany.
					W przypadku braku argument�w przekazanych z linii polece� program skorzysta z domy�lnych
					warto�ci.
					Polecenia podajemy jako pojedyncze ma�e litery, bez my�lnika np. d oznacza dekodowanie.
					Argumenty podajemy w formie nazwa=warto�� np. a=2.
					W przypadku wi�kszej ilo�ci polece� lub argument�w odnosz�cych si� do tego samego pod
					uwag� brany jest jedynie ostatni.
					Je�li argumenty podane do szyfru afinicznego(a,b) s� niepoprawne u�ytkownik zostanie o tym 
					poinformowany, ale program bedzie kontynuuowa� operacj�.
					Program wykorzystuje w�tki do pisania tre�ci plik�w, wi�c zdarzy� si� mo�e, �e g��wny
					program zako�czy si� przed wykonaniem si� w�tku. 
					Przed otworzeniem pliku nale�y poczeka� na stosowny komunikat. 
					Szyfr korzysta z pierwszych 127 znak�w kodu ASCII, co mo�e skutkowa� nietypowymi znakami
					w zaszyfrowanym pliku.
					Program czyta pliki .txt - nie testowano na innych.
					Kryptoanliza z plikiem zaklada, �e podany zostanie plik zawieraj�cy kilka pocz�tkowych 
					liter z pliki �r�d�owego (przed jego zakodowaniem).
					Program dopisuje, nie zast�puje, tre�� do plik�w. 
ARGUMENTY:
nazwa		warto�� domy�lna znaczenie					
a=			2				przesuni�cie kodu afinicznego, tj. ax+b
b=			1				przesuni�cie kodu afinicznego i Cezara
timesDes=	8				ile rund ma wykona� algorytm mini DES
debug		nie				czy logowa� fazy dzia�ania algorytmu
c			tak				kod cezara
a			nie				kod afiniczny
v			nie				szyfr Vinegere'a
x			nie				mini DES
e			tak				szyfrowanie
d			nie				odszyfrowywanie
j			nie				kryptoanaliza z tekstem jawnym
k			nie				kryptoanaliza baz tekstu jawnego
plainFile		plain.txt			plik do szyfrowania
cryptoFile		crypto.txt			plik z zaszyfrowan� tre�ci�
decryptFile		decrypt.txt			plik to od szyfrowaniu
keyFile			key.txt				plik z kluczem
extraFile		extra.txt			plik stosowany przy kryptoanalizie
analyzeFile		analyze.txt			w pliku znajd� si� informacje o krokach 
									wykonywanych przez dany algorytm
					
Zadanie 1
Zrealizowano wszystkie podpunkty. 
Na razie program nie obs�uguje pliku z kluczem (b�dzie to porawione w nast�pnych wersjach).

Przyk�ady:
szyfrujemy szyfrem cezara z przesuni�ciem r�wnym 2
java -jar Krypto.jar c e b=2
deszyfrujemy kod afiniczny z a=1 b=2
java -jar Krypto.jar a d a=1 b=2

Zadanie 2
Program potrafi bezb��dnie zaszyfrowa� i rozszyfrowa� dowolny tekst.
W przypadku kryptoanalizy dla bardziej skomplikowanych tekst�w program "gubi si�" 
i poprawnie t�umaczy tylko fragmenty tekstu. 

Zadanie 3
Wykonano wszystkie 3 podzadania.
By uruchomi� u�ywamy: java -jar Krypto.jar d x debug=true timesDES=8
Je�li chcemy otrzyma� analiz� poszczegolnych krok�w algorytmu musimy 
poda� opcj� "debug=true".
Wyrazy tekstu do przeksztalcenia (w jedn� czy drug� stron�) musz� by� oddzielone 
spacjami- domyslnie tekst wynikowy szyfrowania oddzielony jest znakami nowej linii.