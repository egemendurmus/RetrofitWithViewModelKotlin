# RetrofitWithViewModelKotlin
kotlin'de retrofit kullanarak viewmodel ve parse işlemleri

Model :  Model, uygulama içerisinde kullanacağımız olan datadır. Bu datalar WCF RIA Services ile döndürülen entityler olabileceği gibi doğrudan tanımlanmış POCO nesneleri de olabilir.

View : View, datanın sunulduğu katmandır. Tüm görsellikler View'da yer alır. Kısaca verinin sunulduğu yerdir(Ekran).

ViewModel : ViewModel ise Model ile View'ı bağlayan yapıdır.  View ile Model arasında bir yapıştırıcı görevi görür. View doğrudan ViewModel yardımıyla Model'e erişir ve bazı işlemleri gerçekleştirir.

MVVM(Model, View, View-Model), View’in değişikliklere yanıt verebileceği event-based bir mimari kurmak istersek de karşımıza MVVM çıkacak. Model, MVC’yle aynı. View, viewmodelden sağlanan observable variable’ları bağlar. ViewModel, Model’i wrapleyip View tarafından ihtiyaç duyulan observable datanın sağlanmasından sorumlu.
Yani aslında yine business logic’den model sorumlu. Data değiştiğinde ViewModel bundan haberdar oldu ve View’e haber verdi. View’de ui’ı update etti diyebiliriz basitçe.
