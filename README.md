Sınav Programı Otomasyonu

Bu proje, üniversite düzeyinde bir fakültenin sınav programını yönetmek ve otomatikleştirmek amacıyla geliştirilmiştir. Kullanıcı dostu arayüzü ve güçlü veritabanı altyapısıyla yöneticilere ve asistanlara kapsamlı bir sınav planlama deneyimi sunar.

✨ Özellikler

Yönetici ve asistan kullanıcı giriş sistemi

Yönetici için: Asistan, bölüm, ders, sınav ekleme/çıkarma

Asistan için: Ders ve sınav ekleme/çıkarma

Her iki kullanıcı için: Sınav takvimi görüntüleme

MySQL veritabanı ile senkronize dinamik veri yapısı

Swing GUI ile kullanıcı dostu arayüz

Yetkilendirme ve kullanıcı rolleri

Modüler, genişletilebilir proje yapısı

🛠️ Kullanılan Teknolojiler

Java (Eclipse IDE)

MySQL (Workbench)

Swing (GUI tasarımı)

Kütüphaneler: java.sql, java.util, javax.swing, java.awt.event

🗃️ Proje Yapısı

helper paketi: Veritabanı bağlantıları (ör: DBConnection, Helper sınıfları)

model paketi: Veritabanı tablolarını yansıtan sınıflar (ör: Asistan, Ders, Salon, Sinav vb.)

view paketi: Arayüz sınıfları (ör: LoginGUI, YoneticiGUI, SinavEklemeGUI, SinavGoruntulemeGUI vb.)

🧪 Veritabanı

Veritabanında toplam 9 tablo bulunmaktadır:

Kullanıcılar (Asistan, Yönetici)

Dersler

Sınavlar

Gözetmenler

Sınıflar

Salonlar

Bölümler

Veritabanı tabloları MySQL Workbench aracılığıyla oluşturulmuştur. Projeyle birlikte gelen .sql dosyasıyla hızlıca yüklenebilir.

🔧 Kurulum ve Kullanım

Projeyi Eclipse IDE'ye import edin.

DBConnection.java dosyası üzerinden veritabanı kullanıcı adı, şifre ve bağlantı adresini kendi MySQL ayarlarınıza göre düzenleyin.

Veritabanını oluşturun ve .sql dosyasını içeri aktarın.

Projeyi çalıştırın ve LoginGUI üzerinden giriş yaparak kullanmaya başlayın.

👥 Katkıda Bulunanlar

Bu proje bir ekip çalışmasıdır. 

📎 Diğer GitHub Linkleri

Projeye ait diğer katkı sağlayıcıların GitHub bağlantıları:

https://github.com/HasibeAltuntas06/Projelerim.git

https://github.com/efsayilmaz123/s-navprogram-otomasyonu.git

https://github.com/beyz7/EclipseS-navProgram-.git

Bu sistem, eğitim sektöründeki sınav planlama sürecini kolaylaştırmak amacıyla modern yazılım teknolojileri kullanılarak geliştirilmiştir.

