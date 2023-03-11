## Endpoint: Masukan dan Keluaran
### Bentuk Masukan
API menerima masukan dalam tiga bentuk, yaitu: *parameter*, *query*, dan *body*. Pada dokumen ini *parameter* didefinisikan sebagai masukan yang diikutsertakan dalam bagian *path* dari suatu URI. Contoh:
```url
http://domain/api/v1/posts/<post_id>
```
Token `<post_id>` diganti dengan masukan yang dikehendaki. Masukan dalam bentuk *query* pada dokumen ini didefiniskan sebagai masukan yang diikutsertakan pada bagian *query string* pada suatu URI. Contoh:
```
http://domain/api/v1/posts?post_id=<post_id>
```
Masukan dalam bentuk *body* pada dokumen ini didefinisikan sebagai masukan yang diikutsertakan pada bagian *body* dari suatu *request* HTTP. Masukan dalam bentuk *body* harus menggunakan format JSON. Contoh:
```
{
    post_id: <post_id>
}
```

### Format Pendefinisian Masukan
Semua bentuk masukan, baik dalam bentuk *parameter*, *query*, maupun *body* akan didefinisikan dalam bentuk *JSON Object*. Definisi berupa *JSON Object* menunjukan struktur, tipe, dan format dari masukan suatu *endpoint*. Perhatikan contoh berikut:
```
POST http://domain/api/v1/posts/<post_id>?category_id=<category_id>
{
    "post_id": "integer",
    "category_id": "string|uuidv4",
    "status?": "boolean"
}
```
Definisi yang berikan membutuhkan 3 buah argumen, yaitu: `post_id`, `category_id`, dan `status`. Argumen `post_id` dimasukkan dalam bentuk *parameter* sedangkan argumen `category_id` dimasukkan dalam bentuk *query*. Argumen terakhir, yaitu `status`, tidak muncul pada URI, hal ini menandakan bahwa `status` dimaksudkan untuk diikutsertakan pada *body*. Jika nama suatu argumen diikuti dengan tanda `?`, seperti halnya `status?`, maka argumen tersebut bersifat opsional.

Definsi yang diberikan juga menentukan tipe data dan format masukan, pada definisi ditentukan bahwa argumen `post_id` memiliki tipe data `integer`, sedangkan `category_id` memiliki tipe data `string` dengan format `uuidv4`, dan argumen `status` memiliki tipe data `boolean`.

Pada definsi juga ditentukan bahwa *HTTP method* yang digunakan adalah `POST`.

### Tipe Data dan Format
Berikut adalah tipe data yang mungkin muncul pada definisi masukan:
1. `integer`: bilangan bulat
2. `number`: bilangan ril
3. `string`
4. `boolean`: sebuah *boolean* dengan bentuk literal: `true` atau `false`.
5. `array`
6. `object`

Selain tipe data, definisi juga dapat menentukan format dari masukan, berikut adalah format yang mungkin muncul pada definisi masukan:
1. `string|uuidv4`: sebuah UUID versi 4
2. `string|role`: *string* dengan kemungkinan masukan berupa `admin`, `lecturer`, dan `student`
2. `string|email`
3. `string|time`: format ini menyatakan waktu disertai dengan zona waktu dengan bentuk : `19:15+0500` yang melambangkan pukul 19 lewat 15 dengan zona waktu lebih 5 jam dari GMT.
4. `string|date`: format ini menyatakan tanggal dengan bentuk: `2022-04-12` yang melambangkan tahun 2022 bulan keempat (April) tanggal 12.
5. `string|day`: format ini merupakan hari dalam satu minggu yang dilambangkan dengan: `mon`, `tue`, `wed`, `thu`, `fri`, `sat`, dan `sun` yang menyatakan hari dari Senin hingga Minggu.
6. `string|datetime`: format ini terdiri dari tahun dan waktu disertai zona waktu dengan bentuk: `2022-04-12 19:00+0500` yang melambangkan tahun 2022 bulan keempat (April) tanggal 12 pukul 19:00 dengan zona waktu lebih 5 jam dari GMT.
7. `string|score`: nilai akhir, yaitu: `A`, `B+`, `B`, `B-`, `C+`, `C`, `C-`, `D`, dan `E`.
8. `integer|academic_year`: tahun akademik. Terdiri dari dua buah bilangan bulat, yaitu tahun dan bilangan 1 s/d 3. Bilangan 1 melambangkan semester ganjil, bilangan 2 melambangkan semester genap, sedangkan bilangan 3 melambangkan semester pendek.
9. `integer|initial_year`: tahun masuk mahasiswa. Format sama dengan format `string|academic_year`, kecuali semester hanya dapat diisi dengan 1.
10. `string|order_direction`: arah dari pengurutan. Nilai yang mungkin adalah `asc` untuk pengurutan menaik, dan `desc` untuk pengurutan menurun.

### Format Pendefinisian Masukan: Ekstensi
Pada beberapa definisi juga terdapat aturan tambahan, yaitu: `ACCESS` aturan ini menetapkan `role` dari pengguna yang dapat mengakses suatu *endpoint*. Terdapat juga beberapa aturan tambahan lain yaitu: `SCHEMA`, `ORDER`, dan `FILTER`. Nilai yang mungkin dari aturan `SCHEMA` adalah `Offset-Limit Pagination` dan `Keyset Pagination`. Lihat bagian [Skema Offset-Limit Pagination](#skema-offset-limit-pagination) dan [Skema Keyset Pagination](#skema-keyset-pagination).

### Skema Offset-Limit Pagination
Pada skema ini masukan terdiri dari 4 buah *query*, yaitu: `filter`, `order`, `offset`, dan `limit`. Berikut adalah definisi untuk semua *endpoint* yang menggunakan skema `Offset-Limit Pagination`:
```
{
  "filter?": "object",
  "order?": {
      "column": "string|default:code",
      "direction": "string|order_direction"
  },
  "limit?": "integer|range:1-10|default:5",
  "offset?": "integer|range:1-100|default:0"
}
```

Contoh dari skema ini adalah pada *endpoint* [Melihat Daftar Mata Kuliah](#melihat-daftar-mata-kuliah):
```
ACCESS: all
FILTER: code, name
ORDER: code, name
GET /courses?filter=<filter>&order=<order>&limit=<limit>&offset=<offset>
```
Pada definisi di atas terdapat aturan `FILTER` dan `ORDER`. Aturan `FILTER` mengatur kolom apa saja yang dapat di-*filter*. Contoh-contoh penggunaan:
```
/courses?filter[code]=AIF101
```
```
/courses?filter[code]=AIF101&filter[name]=Pengantar
```

Penyaringan juga dapat dilakukan dengan menggunakan *array*, contohnya dapat dilihat pada *endpoint* [Melihat Daftar Pengguna](#melihat-daftar-pengguna):
```
ACCESS: all
FILTER: name, email, roles*[]
ORDER: name, email
GET /users?filter=<filter>&order=<order>&limit=<limit>&offset=<offset>
```

Pada aturan `FILTER` terdapat kolom `roles` dengan tambahan `[]` dan `*`, tanda `[]` menandakan bahwa kolom tersebut disaring dengan menggunakan sebuah *array*. Contohnya adalah sebagai berikut:
```
/users?filter[roles][]=admin&filter[roles][]=lecturer
```

Tanda `*` pada nama kolom menandakan bahwa penyaringan dilakukan dengan melakukan pencocokan penuh. Pada penyaringan tanpa tanda `*` *string* `"abc"` cocok dengan *string* `"abcd"` karena `"abc"` merupakan *substring* dari `"abcd"`. Pada pencocokan penuh nilai pencari/pembanding harus sama persis dengan nilai yang dicari.

Aturan `ORDER` mengatur kolom apa saja yang dapat diisikan ke *query* `order.column`, atau dengan kata lain kolom apa saja yang dapat digunakan untuk mengurutkan hasil. Contoh penggunaan:
```
/courses?order[column]=code&order[direction]=desc
```
*Query* `limit` menentukan banyaknya *record* atau *item* yang akan dikembalikan oleh *endpoint*, *query* `offset` menentukan banyaknya *record* atau *item* yang akan dilewati (tidak diikutsertakan)

### Skema Keyset Pagination
Pada skema ini masukan terdiri dari 3 buah argumen, yaitu: `filter`, `cursor`, dan `limit`. *Query* `limit` dan `filter` bekerja seperti halnya pada [Skema Offset-Limit Pagination](#skema-offset-limit-pagination). *Query* `cursor` merupakan *string* yang merujuk pada halaman selanjutnya. Keluaran dari *endpoint* yang menggunakan skema ini adalah sebagai berikut:
```
{
  metadata: {
    next: "string"
  }
  data: []
}
```
Properti `data` merupakan sebuah *array* yang berisi data pada suatu halaman. Properti `metadata.next` berisi *cursor* ke halaman berikutnya. Memasukkan isi dari properti `metadata.next` ke dalam *query* `cursor` berarti meminta data untuk halaman berikutnya.

Perubahan pada *query* `limit` dan `filter` mengharuskan mengakibatkan *cursor* yang diterima sebelumnya tidak lagi valid.

### Keluaran: Error
Semua *error* akan memiliki *HTTP status code* 4xx dan 5xx. Berikut adalah bentuk respon server jika terdapat *error*:
```
{
  "errcode": "string",
  "field?": ["string"],
  "message?": "string",
  "reason?": "array"
}
```
Properti `errcode` merupakan kode kesalahan, sedangkan `field` menyatakan argumen-argumen masukan yang menyebabkan kesalahan, `message` merupakan penjelasan dari kesalahan, dan `reason` merupakan pesan kesalahan khusus yang digunakan suatu *endpoint* untuk menjelaskan lebih jauh mengapa suatu kesalahan terjadi.

Berikut adalah penjelasan `errcode`:
* `E_EXIST`: telah ada sebelumnya, atau terdapat duplikat dari data yang harus unik.
* `E_NOT_EXIST`: data yang diminta tidak ada, atau referensi ke suatu entitas (misalnya: `student` dengan `student_id`) tidak ditemukan.
* `E_DUPLICATE`: perubahan yang diminta akan menimbulkan adanya duplikat pada *field* di mana data yang tersimpan harus unik, error ini akan dilengkapi oleh keterangan *field* penyebab *error*.
* `E_OVERLAPPING_SCHEDULE`
* `E_INVALID_SETTINGS`
* `E_INVALID_TYPE`
* `E_INVALID_VALUE`
* `E_INVALID_FORMAT`
* `E_MISSING_ARGUMENT`
* `E_FORBIDDEN_ARGUMENT`
* `E_NO_ARGUMENT`
* `E_UNKNOWN`: terjadi suatu *error* yang tidak diketahui penyebabnya.
* `E_UNKNOWN_FATAL`: terjadi suatu *error* yang tidak diketahui penyebabnya.

Khusus `E_INVALID_TYPE`, `E_INVALID_VALUE`, `E_INVALID_FORMAT`, `E_MISSING_ARGUMENT`, dan `E_FORBIDDEN_ARGUMENT`  tidak disebutkan kembali pada penjelasan dan definisi setiap *endpoint*. Kelima `errcode` merupakan *error* yang terjadi jika *request* tidak mengikuti definisi yang telah diberikan. Kesalahan `E_UNKNOWN` dan `E_UNKNOWN_FATAL` juga tidak dideskripsikan kembali karena merupakan kesalahan yang tidak diketahui penyebabnya.

## Endpoint: Academic Year
### Mengubah Academic Year Aktif
Dengan mengubah tahun akademik aktif, sistem akan membuat 21 semester ke depan, diawali dengan tahun akademik aktif.

Request:
* `year`: tahun
* `semester`: nilai 1 menyatakan semester ganjil, nilai 2 menyatakan semester genap, dan nilai 3 menyatakan semester pendek.

```
ACCESS: admin
PATCH /academic-years/active-year
{
  "year": "integer|range:1970-9000",
  "semester": "integer|range:1-3"
}
```

Success:
```
{
  "active_year": "string|academic_year"
}
```

### Melihat Daftar Academic Year
Request:
```
ACCESS: admin
GET /academic-years
```

Success:
```
{
  "active_year": "string|academic_year",
  "academic_years": [
      "string|academic_year"
  ]
}
```

Error:
* `E_INVALID_SETTINGS`: kesalahan pada konfigurasi *server*.

## Endpoint: Course
### Membuat Mata Kuliah
Membuat mata kuliah baru.

Request:
* `name`: nama mata kuliah
* `code`: kode mata kuliah
* `semester`: semester di mana mata kuliah dibuka (semester 1 hingga semester 8).

```
ACCESS: admin
POST /courses
{
  "name": "string|length:1-256",
  "code": "string|length:5-15",
  "semester": "integer|range:1-8"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "code": "string|length:5-15",
  "name": "string|length:1-256",
  "semester": "integer|range:1-8"
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_DUPLICATE` (*field* `code`): perubahan yang diminta akan menimbulkan adanya duplikat pada *field* `code`.

### Mengubah Mata Kuliah
Mengubah mata kuliah yang direferensikan oleh *parameter* `id`.

Request:
* `id`: *id* referensi mata kuliah
* `name`: nama mata kuliah (opsional).
* `code`: kode mata kuliah (opsional).
* `semester`: semester mata kuliah akan dibuka (semester 1 hingga semester 8) (opsional).

```
ACCESS: admin
PATCH /courses/<id>
{
  "id": "string|uuidv4",
  "name?": "string|length:1-256",
  "code?": "string|length:5-15",
  "semester?": "integer|range:1-8",
  "archived_at": "string|datetime|nullable"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "code": "string|length:5-15",
  "name": "string|length:1-256",
  "semester": "integer|range:1-8",
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NO_ARGUMENT`: tidak ada argumen yang diberikan untuk mengubah mata kuliah baik `name`, `code`, maupun `semester`.
* `E_NOT_EXIST`: mata kuliah, yang direferensikan oleh `id`, tidak ada.
* `E_DUPLICATE` (*field* `code`): perubahan yang diminta akan menimbulkan adanya duplikat pada *field* `code`.

### Mengarsipkan Mata Kuliah
Mengarsipkan mata kuliah yang direferensikan oleh *parameter* `id`.

Request:
* `id`: *id* referensi mata kuliah

```
ACCESS: admin
PATCH /courses/<id>/action/archive
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "code": "string|length:5-15",
  "name": "string|length:1-256",
  "semester": "integer|range:1-8"
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: mata kuliah tidak ditemukan.

### Mengembalikan Mata Kuliah Terarsipkan
Mengembalikan mata kuliah yang direferensikan oleh *parameter* `id`.

Request:
* `id`: *id* referensi mata kuliah.

```
ACCESS: admin
PATCH /courses/<id>/action/restore
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "code": "string|length:5-15",
  "name": "string|length:1-256",
  "semester": "integer|range:1-8"
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: mata kuliah tidak ditemukan.

### Melihat Daftar Mata Kuliah
Request:
```
ACCESS: all
SCHEMA: Offset-Limit Pagination
FILTER: code, name
ORDER: code, name
GET /courses?filter=<filter>&order=<order>&limit=<limit>&offset=<offset>
```

Success:
```
[
  {
    "id": "string|uuidv4",
    "code": "string|length:5-15",
    "name": "string|length:1-256",
    "semester": "integer|range:1-8"
    "archived_at": "string|datetime|nullable"
  }
]
```

### Membuat Prasyarat Mata Kuliah
Membuat prasyarat sebuah mata kuliah yang direferensikan oleh `course_id`, dengan syarat mata kuliah yang direferensikan oleh `prereq_id` dengan nilai minimal yang ditentukan oleh `score`.

Request:
* `course_id`: *id* referensi mata kuliah yang diberi syarat.
* `prereq_id`: *id* referensi mata kuliah yang menjadi prasyarat.
* `score`: nilai minimal

```
ACCESS: admin
POST /courses/<course_id>/prerequisites/<prereq_id>
{
  "course_id": "string|uuidv4",
  "prereq_id": "string|uuidv4",
  "score": "string|score"
}
```

Success:
```
{
  "course_id": "string|uuidv4",
  "prereq_id": "string|uuidv4",
  "score": "string|score"
}
```

Error:
* `E_EXIST`: prasyarat telah ada sebelumnya.
* `E_NOT_EXIST` (*field* `course_id`): mata kuliah yang direferensikan oleh `course_id` tidak ada.
* `E_NOT_EXIST` (*field* `prereq_id`): mata kuliah yang direferensikan oleh `prereq_id` tidak ada.

### Menghapus Prasyarat Mata Kuliah
Menghapus prasyarat sebuah mata kuliah yang direferensikan oleh `course_id`, dengan syarat mata kuliah yang direferensikan oleh `prereq_id`.

Request:
* `course_id`: *id* referensi mata kuliah yang diberi syarat.
* `prereq_id`: *id* referensi mata kuliah yang menjadi prasyarat.

```
ACCESS: admin
DELETE /courses/<course_id>/prerequisites/<prereq_id>
{
  "course_id": "string|uuidv4",
  "prereq_id": "string|uuidv4"
}
```

Success:
```
{
  "course_id": "string|uuidv4",
  "prereq_id": "string|uuidv4",
  "score": "string|score"
}
```

Error:
* `E_NOT_EXIST`: prasyarat tidak ditemukan.

### Melihat Daftar Prasyarat Mata Kuliah
```
GET /courses/<course_id>/prerequisites
{
  "course_id": "string|uuidv4"
}
```

Success:
```
[
  {
    "course_id": "string|uuidv4",
    "prereq_id": "string|uuidv4",
    "score": "string|score",
    "prerequisite_code": "string|length:5-15",
    "prerequisite_name": "string|length:1-256",
    "prerequisite_semester": "integer|range:1-8",
    "prerequisite_archived_at": "string|datetime"
  }
]
```


## Endpoint: Authentication
```
POST /authenticate
{
  "email": "string|email",
  "password": "string",
  "role": "string"
}
```

Success:
```
{
  token: "string"
}
```

Error:
* `E_AUTH_FAILED`

## Endpoint: User
### Menambah Pengguna
Jika `roles` dari pengguna tidak mengandung *role* "student":

Request:
* `name`: nama pengguna
* `email`: alamat email pengguna
* `password`: password untuk akun pengguna
* `roles`: *role* yang diberikan untuk pengguna 

```
ACCESS: admin
POST /users
{
  "name": "string|length:1-256",
  "email": "string|email",
  "password": "string",
  "roles": [
    "string|role"
  ]
}
```

Jika `roles` dari pengguna mengandung *role* "student":
Request:
* `name`: nama pengguna
* `email`: alamat email pengguna
* `password`: password untuk akun pengguna
* `roles`: *role* yang diberikan untuk pengguna 
* `npm`: NPM dari mahasiswa
* `initial_year`: tahun masuk mahasiswa (dengan format khusus, yaitu format `initial_year`)

```
ACCESS: admin
POST /users
{
    "name": "string|length:1-256",
    "email": "string|email",
    "roles": [
      "string|role"
    ],
    "npm": "string|length:1-16",
    "initial_year": "integer|range:1970-9000"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "name": "string|length:1-256",
  "email": "string|email",
  "roles": [
    "string|role"
  ],
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_DUPLICATE` (*field* `email`): email telah digunakan akun lain.
* `E_NOT_EXIST` (*field* `initial_year`): tahun akademik masuk tidak ditemukan.
* `E_EXIST` (*field* `npm`): NPM telah digunakan oleh mahasiswa lain.

### Mengubah Pengguna
Request:
* `name`: nama baru.
* `email`: alamat email baru.
```
ACCESS: admin
PATCH /users/<id>
{
    "id": "string|uuidv4",
    "name?": "string|length:1-256",
    "email?": "string|email"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "name": "string|length:1-256",
  "email": "string|email"
}
```

Error:
* `E_DUPLICATE` (*field* `email`): perubahan yang diminta akan menimbulkan adanya duplikat pada *field* `email`.
* `E_NO_ARGUMENT`: tidak ada argumen untuk melakukan pengubahan baik `name` maupun `email`.

### Mengarsipkan Pengguna
Menandai bahwah pengguna telah diarsipkan.

Request:
* `id`: *id* dari pengguna yang hendak diarsipkan
```
ACCESS: admin
PATCH /users/<id>/action/archive
{
  "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4"
  "name": "string|length:1-256",
  "email": "string|email",
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: pengguna tidak ada.

### Mengembalikan Pengguna Terarsipkan
Request:
* `id`: *id* dari pengguna yang hendak dikembalikan dari arsip
```
ACCESS: admin
PATCH /users/<id>/action/restore
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "name": "string|length:1-256",
  "email": "string|email",
  "roles": [
      "string|role"
  ],
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: pengguna tidak ada.

### Melihat Pengguna Berdasarkan Email
Request:
* `email`: alamat email dari pengguna yang dicari.
```
ACCESS: all
GET /users/email/<email>
{
    "email": "string|email"
}
```

Success:
```
{
  "name": "string|length:1-256",
  "email": "string|email",
  "roles": [
      "string|role"
  ],
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: pengguna tidak ada.

### Melihat Pengguna Berdasarkan Id
Request:
```
ACCESS: all
GET /users/id/<id>
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "name": "string|length:1-256",
  "email": "string|email",
  "roles": [
      "string|role"
  ],
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: pengguna tidak ada.

### Melihat Pengguna Berdasarkan Token Otentikasi
Request:
```
ACCESS: all
GET /users/self
```

Success:
```
{
  "name": "string|length:1-256",
  "email": "string|email",
  "roles": [
      "string|role"
  ],
  "archived_at": "string|datetime|nullable"
}
```

Error:
* `E_NOT_EXIST`: pengguna tidak ada.

### Melihat Daftar Pengguna
Request:
Lihat bagian [Skema Offset Limit Pagination](#skema-offset-limit-pagination)
```
ACCESS: all
FILTER: name, email, roles*[]
ORDER: name, email
GET /users?filter=<filter>&order=<order>&limit=<limit>&offset=<offset>
```

Success: 
```
[
  {
    "name": "string|length:1-256",
    "email": "string|email",
    "roles": [
        "string|role"
    ],
    "archived_at": "string|datetime|nullable"
  }
]
```

Error:
* `E_NOT_EXIST`: pengguna tidak ada.

### Menambah Role Pengguna
Request:
Jika `roles` tidak mengandung *role* "student":
* `user_id`: *id* pengguna yang hendak ditambahkan *role*-nya.
* `roles`: *array* yang berisi *role*-*role* yang hendak ditambahkan.
```
ACCESS: admin
POST /users/<id>/roles/create
{
    "user_id": "string|uuidv4",
    "roles": [
        "string|role"
    ]
}
```

Jika `roles` mengandung *role* "student":
* `user_id`: *id* pengguna yang hendak ditambahkan *role*-nya.
* `roles`: *array* yang berisi *role*-*role* yang hendak ditambahkan.
* `npm`: NPM untuk mahasiswa yang hendak ditambahkan (menambah *role* "student" berarti mendaftarkan mahasiswa baru).
* `initial_year`: tahun akademik masuk untuk mahasiswa yang hendak ditambahkan (menambah *role* "student" berarti mendaftarkan mahasiswa baru).
```
ACCESS: admin
POST /users/<id>/roles/create
{
    "user_id": "string|uuidv4",
    "roles": [
        "string|role"
    ],
    "npm": "string|range:1-6",
    "initial_year": "integer|initial_year"
}
```

Success:
```
[
  "string|role"
]
```

Error:
* `E_NOT_EXIST` (*field* `user_id`): pengguna tidak ditemukan.
* `E_NOT_EXIST` (*field* `initial_year`): tahun akademik masuk tidak ditemukan.
* `E_EXIST` (*field* `npm`): NPM telah digunakan oleh mahasiswa lain.
* `E_EXIST`: *role* untuk pengguna tersebut telah ada sebelumnya.


### Menghapus Role Pengguna
Request:
* `user_id`: *id* pengguna yang hendak dihapus *role*-nya.
* `roles`: *array* yang berisi *role*-*role* yang hendak dihapus.
```
ACCESS: admin
POST /users/<user_id>/roles/delete
{
    "user_id": "string|uuidv4",
    "roles": [
        "string|role"
    ]
}
```

Success:
```
[
  "string|role"
]
```

Error:
* `E_NOT_EXIST`: *role* untuk pengguna tersebut tidak ditemukan.

## Endpoint: Announcement
### Membuat Announcement
```
ACCESS: admin
POST /announcements
{
  "title": "string|length:1-256",
  "content": "string|length:1-5000",
  "tags": [
    "string|uuidv4" /* id dari tag */
  ]
}
```

Success:
```
{
  "id": "string|uuidv4",
  "title": "string|length:1-256",
  "content": "string|length:1-5000",
  "author_id": "string|uuidv4",
  "created_at": "string|datetime",
  "updated_at": "string|datetime",
  "tags": [
    {
      "announcement_id": "string|uuidv4",
      "tag_id": "string|uuidv4"
    }
  ]
}
```

### Mengubah Announcement
```
ACCESS: admin
PATCH /announcements/<id>
{
  "id": "string|uuidv4",
  "content": "string|length:1-5000"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "title": "string|length:1-256",
  "content": "string|length:1-5000",
  "author_id": "string|uuidv4",
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
}
```

### Menambah Tag Announcement
Request:
* `id`: *id* dari pengumuman
* `tags`: *array* dari *id* tag yang hendak ditambahkan
```
ACCESS: admin
POST /announcements/<announcement_id>/tags
{
  "announcement_id": "string|uuidv4",
  "tags": [
    "string|uuidv4"
  ]
}
```

Success:
```
[
  {
    "announcement_id": "string|uuidv4",
    "tag_id": "string|uuidv4"
  }
]
```

### Menghapus Tag Announcement
Request:
* `id`: *id* dari pengumuman
* `tag_id`: *id* tag yang hendak dihapus
```
ACCESS: admin
POST /announcements/<announcement_id>/tags/<tag_id>
{
  "announcement_id": "string|uuidv4",
  "tag_id": "string|uuidv4",
}
```

Success:
```
{
  "announcement_id": "string|uuidv4",
  "tag_id": "string|uuidv4"
}
```

### Menghapus Announcement
```
ACCESS: admin
DELETE /announcements/<id>
{
  "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "title": "string|length:1-256",
  "content": "string|length:1-5000",
  "author_id": "string|uuidv4",
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
}
```

### Melihat Announcement
```
ACCESS: admin
GET /announcements/<id>
{
  "id": "string|uuidv4",
  "content": "string|length:1-5000"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "title": "string|length:1-256",
  "content": "string|length:1-5000",
  "author_id": "string|uuidv4",
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
  "author": {
    "id": "string|uuidv4", /* id penulis */
    "author": "string|length:1-256" /* nama penulis */
  },
  "tags": [
    {
      "tag": "string|length:1-256",
      "tag_id": "string|uuidv4"
    }
  ]
}
}
```

### Melihat Daftar Announcement
Request (Filter):
* `title`: judul pengumuman
* `tags`: *id* tag (uuidv4)
```
ACCESS: all
SCHEMA: Keyset Pagination
FILTER: title, tags*[]
GET /announcements?filter=<filter>&cursor=<cursor>&limit=<limit>
{
  "filter?": "object",
  "cursor?": "string",
  "limit?": "integer|1-10"
}
```

Success:
```
{
  "metadata": {
    "next": "string" /* cursor halaman berikutnya */
  },
  "data": [
    {
      "id": "string|uuidv4",
      "title": "string|length:1-256",
      "updated_at": "string|datetime",
      "created_at": "string|datetime",
      "author": {
        "id": "string|uuidv4",
        "author": "string|length:1-256"
      },
      "tags": [
        {
          "tag": "string|length:1-256",
          "tag_id": "string|uuidv4"
        }
      ]
    }
  ]
}
```

## Endpoint: Student
### Mengubah Student
Request:
* `npm`: NPM baru.
* `initial_year`: tahun masuk baru.
```
ACCESS: admin
PATCH /student/<user_id>
{
  "user_id": "string|uuidv4",
  "npm?": "string|length:1-16",
  "initial_year?": "integer|range:1970-9000" /* tahun awal masuk */
}
```

Success:
```
{
  "user_id": "string|uuidv4",
  "npm": "string|length:1-16",
  "initial_year": "integer|range:1970-9000" /* tahun awal masuk */
}
```

Error:
* `E_DUPLICATE` (*field* `npm`): NPM telah digunakan.
* `E_NOT_EXIST` (*field* `initial_year`): tahun akademik masuk tidak ditemukan.
* `E_NO_ARGUMENT`: tidak ada argumen untuk melakukan pengubahan baik `name` maupun `email`.

### Melihat Student Berdasarkan Email
Request:
* `email`: alamat email dari mahasiswa yang dicari
```
ACCESS: all
GET /student/email/<email>
{
    "email": "string|email",
}
```

Success:
```
{
  "user_id": "string|uuidv4",
  "name": "string|length:1-256",
  "email": "string|email",
  "npm": "string|length:1-16",
  "initial_year": "integer|range:1970-9000" /* tahun awal masuk */
}
```

Error:
* `E_NOT_EXIST`

### Melihat Student Berdasarkan Id
Request:
* `email`: *id* dari mahasiswa yang dicari
```
ACCESS: all
GET /student/id/<id>
{
    "id": "string|uuidv4",
}
```

Success:
```
{
  "user_id": "string|uuidv4",
  "name": "string|length:1-256",
  "email": "string|email",
  "npm": "string|length:1-16",
  "initial_year": "integer|range:1970-9000" /* tahun awal masuk */
}
```

Error:
* `E_NOT_EXIST`

### Melihat Student Berdasarkan NPM
Request:
* `npm`: NPM dari mahasiswa yang dicari
```
ACCESS: all
GET /student/npm/<npm>
{
    "npm": "string|range:1-16",
}
```

Success:
```
{
  "user_id": "string|uuidv4",
  "name": "string|length:1-256",
  "email": "string|email",
  "npm": "string|length:1-16",
  "initial_year": "integer|range:1970-9000"
}
```

Error:
* `E_NOT_EXIST`

### Melihat Daftar Student
Request:
```
ACCESS: all
FILTER: name, email, npm, initial_year*
ORDER: name, email, npm, initial_year
```

Success:
```
{
  "id": "string|uuidv4",
  "name": "string|length:1-256",
  "email": "string|email",
  "archived_at": "string|datetime"
  "npm": "string|length:1-16",
  "initial_year": "integer|range:1970-9000" /* tahun awal masuk */
}
```

## Endpoint: Tag
### Membuat Tag
```
ACCESS: admin
POST /tags
{
  "tag": "string|length:1-256"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "tag": "string|length:1-256"
}
```

Error:
* `E_DUPLICATE` (*field* `tag`)

### Menghapus Tag
```
ACCESS: admin
DELETE /tags/<id>
{
  "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "tag": "string|length:1-256"
}
```

Error:
* `E_NOT_EXIST`

### Mengubah Tag
```
ACCESS: admin
PATCH /tags/<id>
{
    "id": "string|uuidv4",
    "tag": "string|length:1-256"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "tag": "string|length:1-256"
}
```

Error:
* `E_NOT_EXIST`
* `E_DUPLICATE` (*field* `tag`)

### Melihat Daftar Tag
```
ACCESS: all
GET /tags
```

Success:
```
[
  {
    "id": "string|uuidv4",
    "tag": "string|length:1-256"
  }
]
```

## Endpoint: Lecturer Time Slot
### Membuat Slot
Request:
* `day`: hari
* `start_time`: waktu awal
* `end_time`: waktu akhir
```
ACCESS: lecturer
POST /lecturer-time-slots
{
  "day": "string|day",
  "start_time": "string|time",
  "end_time": "string|time"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "lecturer_id": "string|uuidv4",
  "day": "string|day",
  "start_time": "string|time",
  "end_time": "string|time",
  "created_at": "string|datetime",
  "updated_at": "string|datetime",
}
```

Error:
* `E_OVERLAPPING_SCHEDULE`

### Menghapus Slot
```
ACCESS: lecturer
DELETE /lecturer-time-slots/<id>
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4",
  "lecturer_id": "string|uuidv4",
  "day": "string|day",
  "start_time": "string|time",
  "end_time": "string|time",
  "created_at": "string|datetime",
  "updated_at": "string|datetime",
}
```

Error:
* `E_UNAUTHORIZED`: hanya dosen yang membuat slot yang dapat menghapusnya kembali

### Melihat Daftar Slot
```
ACCESS: all
GET /lecturer-time-slots/lecturers/<lecturer_id>
{
    "lecturer_id": "string|uuidv4"
}
```

Success:
```
[
  {
    "id": "string|uuidv4",
    "lecturer_id": "string|uuidv4",
    "day": "string|day",
    "start_time": "string|time",
    "end_time": "string|time",
    "created_at": "string|datetime",
    "updated_at": "string|datetime",
  }
]
```

## Endpoint: Appointment
Pada bagian ini akan dijelaskan *endpoint*-*endpoint* yang berkenaan dengan *appointment* (janji temu).

### Membuat Appointment
Request:
* `title`: judul dari *appointment*.
* `description`: deskripsi tambahan dari *appointment* (opsional).
* `start_datetime`: waktu dan tanggal mulai *appointment*.
* `end_datetime`: waktu dan tanggal akhir *appointment*.

```
ACCESS: all
POST /appointments
{
    "title": "string|length:1-256",
    "description?": "string|length:1-5000",
    "start_datetime": "string|datetime",
    "end_datetime": "string|datetime"
}
```

Success:
```
{
  "id": "string|uuidv4"
  "title": "string|length:1-256",
  "description": "string|length:1-5000|nullable",
  "start_datetime": "string|datetime",
  "end_datetime": "string|datetime"
  "organizer_id": "string|uuidv4", /* id penyelenggara */
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
}
```

Error:
* `E_OVERLAPPING_SCHEDULE`: kesalahan ini terjadi karena penyelenggara dari *appointment* memiliki *appointment* lain yang jadwalnya bertabrakan. Kesalahan ini juga dapat terjadi jika penyelenggara menjadi partisipan dari *appointment* orang lain dan telah menyatakan kesediaannya untuk hadir.
* `E_NOT_EXIST` (*field* `organizer_id`): `organizer_id` adalah referensi kepada pengguna yang menjadi penyelenggara *appointment*, `organizer_id` diambil dari *token* pengirim *request*.

### Mengubah Appointment
*Appointment* hanya dapat diubah oleh pengguna pembuat *appointment* tersebut. Dalam melakukan pengubahan apapun pada *appointment* terdapat dua aturan yang berlaku:
1. Argument `start_datetime` dan `end_datetime` wajib dikirimkan kembali walaupun tidak berubah.
2. Pemanggilan *appointment* akan menyebabkan semua kesediaan partisipan untuk hadir menjadi tidak berlaku.

Request:
* `id`: *id* yang mereferensikan *appointment* yang hendak diubah.
* `title`: judul dari *appointment*.
* `description`: deskripsi tambahan dari *appointment* (opsional).
* `start_datetime`: waktu dan tanggal mulai *appointment*.
* `end_datetime`: waktu dan tanggal akhir *appointment*.

```
ACCESS: all (owner/organizer)
PATCH /appointments/<id>
{
    "id": "string|uuidv4",
    "title?": "string|length:1-256",
    "description?": "string|length:1-5000",
    "start_datetime": "string|datetime",
    "end_datetime": "string|datetime"
}
```

Success:
```
{
  "id": "string|uuidv4"
  "title": "string|length:1-256",
  "description": "string|length:1-5000|nullable",
  "start_datetime": "string|datetime",
  "end_datetime": "string|datetime"
  "organizer_id": "string|uuidv4", /* id penyelenggara */
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
}
```

Error:
* `E_OVERLAPPING_SCHEDULE`: kesalahan ini terjadi karena penyelenggara dari *appointment* memiliki *appointment* lain, atau jika penyelenggara menjadi partisipan dari *appointment* orang lain dan telah menyatakan kesediaannya untuk hadir, yang mana jadwalnya bertabrakan. 
* `E_NOT_EXIST`: *appointment* tidak ditemukan.

### Menghapus Appointment
*Appointment* hanya dapat dihapus oleh pengguna pembuat *appointment* tersebut.

Request:
* `id`: *id* referensi ke *appointment* yang hendak dihapus.

```
ACCESS: all (owner/organizer)
DELETE /appointments/<id>
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4"
  "title": "string|length:1-256",
  "description": "string|length:1-5000|nullable",
  "start_datetime": "string|datetime",
  "end_datetime": "string|datetime"
  "organizer_id": "string|uuidv4", /* id penyelenggara */
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
}
```

Error:
* `E_NOT_EXIST`: *appointment* tidak ditemukan.

### Melihat Appointment
Request:
* `id`: *id* referensi ke *appointment* yang hendak dilihat.

```
ACCESS: all
GET /appointments/<id>
{
    "id": "string|uuidv4"
}
```

Success:
```
{
  "id": "string|uuidv4"
  "title": "string|length:1-256",
  "description": "string|length:1-5000|nullable",
  "start_datetime": "string|datetime",
  "end_datetime": "string|datetime"
  "organizer_id": "string|uuidv4", /* id penyelenggara */
  "organizer_name": "string|length:1-256", /* nama penyelenggara */
  "created_at": "string|datetime",
  "updated_at": "string|datetime"
}
```

Error:
* `E_NOT_EXIST`: *appointment* tidak ditemukan.

### Melihat Daftar Appointment
Melihat daftar *appointment* milik sendiri atau di mana pengguna yang direferensikan oleh `user_id` merupakan partisipan dari *appointment* tersebut.

Request:
* `user_id`: *id* dari pengguna yang hendak dilihat *appointment*-nya.
* `start_date`: batas tanggal awal pencarian.
* `end_date`: batas tanggal akhir pencarian.

```
ACCESS: all
GET /appointments/users/<user_id>/start-date/<start_date>/end-date/<end_date>
{
    "user_id": "string|uuidv4",
    "start_date": "string|date",
    "end_date": "string|date"
}
```

Success:
```
[
  {
    "start_datetime": "string|datetime",
    "end_datetime": "string|datetime"
  }
]
```

Error:
* `E_INVALID_VALUE`: jarak antara `start_date` dan `end_date` lebih dari 7 hari.

### Melihat Daftar Appointment (Owner dan Participant)
Melihat daftar *appointment* milik sendiri atau di mana pengguna (dengan *id* yang diambil dari *token*) merupakan partisipan dari *appointment* tersebut.

Request:
* `start_date`: batas tanggal awal pencarian.
* `end_date`: batas tanggal akhir pencarian.

```
ACCESS: all
GET /appointments/start-date/<start_date>/end-date/<end_date>
{
    "start_date": "string|date",
    "end_date": "string|date"
}
```

Success:
```
[
  {
    "id": "string|uuidv4"
    "title": "string|length:1-256",
    "description": "string|length:1-5000|nullable",
    "start_datetime": "string|datetime",
    "end_datetime": "string|datetime"
    "organizer_id": "string|uuidv4", /* id penyelenggara */
    "organizer_name": "string|length:1-256", /* nama penyelenggara */
    "created_at": "string|datetime",
    "updated_at": "string|datetime"
  }
]
```

Error:
* `E_INVALID_VALUE`: jarak antara `start_date` dan `end_date` lebih dari 7 hari.

### Membuat Peserta Appointment
*Endpoint* ini digunakan untuk membuat peserta *appointment* atau dengan kata lain mengundang orang lain untuk menjadi partisipan dari *appointment*. Hal ini hanya dapat dilakukan oleh pemilik/penyelenggara *appointment*.

Request:
* `appointment_id`: merupakan *id* dari *appointment*.
* `participants`: merupakan *array* yang berisi *id* dari pengguna yang hendak diundang.

```
ACCESS: all (owner/organizer)
POST /appointments/<appointment_id>/participants
{
    "appointment_id": "string|uuidv4",
    "participants": [
        "string|uuidv4"
    ]
}
```

```
Success:
[
  {
    "appointment_id": "string|uuidv4",
    "participant_id": "string|uuidv4",
    "attending": "boolean|nullable",
    "created_at": "string|datetime",
    "updated_at": "string|datetime"
  }
]
```

Error:
* `E_NOT_EXIST` (*field* `appointment_id`): *appointment* tidak ditemukan.
* `E_NOT_EXIST` (*field* `participant_id`): salah satu atau lebih pengguna yang diundang tidak ditemukan.
* `E_EXIST`: undangan untuk salah satu pengguna (pada *field* `participants`) telah ada sebelumnya.
* `E_UNAUTHORIZED`

### Menghapus Peserta Appointment
*Endpoint* ini digunakan untuk menghapus peserta *appointment* atau dengan kata lain menghapus undangan untuk menjadi partisipan dari *appointment*. Hal ini hanya dapat dilakukan oleh pemilik/penyelenggara *appointment*.

Request:
* `id`: merupakan *id* dari *appointment*.
* `participants`: merupakan *array* yang berisi *id* pengguna dari undangan yang hendak dihapus.

```
ACCESS: all (owner/organizer)
POST /appointments/<id>/participants/delete
{
    "id": "string|uuidv4",
    "participants": [
        "string|uuidv4"
    ]
}
```
Success:
[
  {
    "appointment_id": "string|uuidv4",
    "participant_id": "string|uuidv4",
    "attending": "boolean|nullable",
    "created_at": "string|datetime",
    "updated_at": "string|datetime"
  }
]

Error:
* `E_NOT_EXIST`: salah satu peserta tidak ditemukan.
* `E_UNAUTHORIZED`

### Mengubah Peserta Appointment
*Endpoint* ini digunakan untuk mengubah status kehadiran (RSVP) dari undangan. Hal ini hanya dapat dilakukan oleh orang yang diundang (*invitee*).

Request:
* `appointment_id`: merupakan *id* dari *appointment*.
* `participant_id`: merupakan *id* dari pengguna yang diundang.
* `attending`: status kehadiran.

```
ACCESS: all (invitee)
PATCH /appointments/<appointment_id>/participants/<participant_id>
{
    "appointment_id": "string|uuidv4",
    "participant_id": "string|uuidv4",
    "attending": "boolean"
}
```
Success:
[
  {
    "appointment_id": "string|uuidv4",
    "participant_id": "string|uuidv4",
    "attending": "boolean|nullable",
    "created_at": "string|datetime",
    "updated_at": "string|datetime"
  }
]

Error:
* `E_NOT_EXIST`: salah satu peserta tidak ditemukan.
* `E_OVERLAPPING_SCHEDULE`: salah satu peserta tidak ditemukan.
* `E_UNAUTHORIZED`

### Melihat Daftar Peserta Appointment
Melihat daftar peserta suatu *appointment*. Hanya dapat dilakukan oleh penyelenggara.
```
ACCESS: all (owner/organizer)
GET /appointments/<appointment_id>/participants
{
  "appointment_id": "string|uuidv4"
}
```

Success:
```
[
  {
    "id": "string|uuidv4", /* id pengguna */
    "name": "string|length:1-256",
    "attending": "boolean",
    "created_at": "string|datetime",
    "updated_at": "string|datetime"
  }
]
```

### Melihat Daftar Undangan Appointment
Melihat semua undangan yang diterima seorang pengguna.
```
ACCESS: all
GET /appointments/invitations
```
Success:
```
[
  {
    "appointment_id": "string|uuidv4",
    "title": "string|length:1-256",
    "description": "string|length:1-5000|nullable",
    "attending": "boolean",
    "created_at": "string|datetime",
    "updated_at": "string|datetime"
  }
]
```

## Endpoint: Enrolment
*Endpoint*-*endpoint* pada bagian ini berkenaan dengan proses *enrolment* (pengambilan suatu mata kuliah oleh mahasiswa). Selain itu, pada bagian ini juga dijelaskan *endpoint*-*endpoint* yang digunakan untuk melakukan proses *locking*. Pada dokumen ini *locking* didefinisikan sebagai penutupan perubahan data *enrolment*. Terdapat dua jenis *lock*, yaitu:
  * *Stage-1 Locking*: *lock* jenis ini akan menghalangi mahasiswa untuk menambah *enrolment* baru.
  * *Stage-2 Locking*: *lock* jenis ini akan menghalangi mahasiswa untuk mengubah nilai mata kuliah pada *enrolment*.

### Menambah Enrolment
Digunakan untuk memasukkan data *enrolment*. Referensi (berupa *id*) mahasiswa yang hendak melakukan *enrolment* tidak secara eksplisit dimasukkan dari *request* melainkan diambil dari *token* (yang didapatkan ketika *login*) yang dikirimkan bersama dengan *request*.

*Enrolment* dapat gagal (diluar kesalahan biasa) atas tiga alasan:
1. *Enrolment* yang hendak dilakukan untuk suatu tahun akademik berbeda dengan tahun akademik saat ini (`E_LOCKED`);
2. Terdapat kunci *lock* tipe *stage-1* untuk mahasiswa dan tahun akademik tersebut (`E_LOCKED`).
3. Terdapat prasyarat yang belum terpenuhi (`E_UNSATISFIED_PREREQUSITE`);

Request:
* `course_id`: *id* dari mata kuliah yang hendak diambil oleh mahasiswa.
* `academic_year`: tahun akademik di mana mahasiswa mengambil mata kuliah.

```
ACCESS: student
POST /enrolments
{
    "course_id": "string|uuidv4",
    "academic_year": "string|academic_year",
}
```

Success:
***Not Yet Specified***

Error:
* `E_INVALID_SETTINGS`: terdapat kesalahan pada konfigurasi *server*.
* `E_LOCKED`: pembuatan *enrolment* gagal karena terdapat kunci *stage-1* atau *enrolment* dilakukan untuk suatu tahun akademik yang berbeda dengan tahun akademik yang aktif saat ini.
* `E_UNSATISFIED_PREREQUISITE`: terdapat prasyarat mata kuliah yang belum diambil atau telah diambil namun belum lulus (jika persyaratan menentukan batas nilai minimal). Kesalahan ini juga akan memiliki properti `reason` yang berisi mata kuliah prasyarat yang belum terpenuhi.
* `E_NOT_EXIST` (*field* `academic_year`): tahun akademik tidak ditemukan.
* `E_NOT_EXIST` (*field* `course_id`): mata kuliah tidak ditemukan.
* `E_NOT_EXIST` (*field* `student_id`): mahasiswa tidak ditemukan, `student_id` diambil dari *token*.
* `E_EXIST`: data *enrolment* telah ada sebelumnya.


### Mengubah Enrolment
Digunakan oleh mahasiswa untuk mengubah nilai pada akhir semester.

Pengubahan nilai dapat gagal (diluar kesalahan biasa) karena dua alasan:
1.  *Enrolment* yang hendak diubah adalah untuk suatu tahun akademik berbeda dengan tahun akademik saat ini (`E_LOCKED`);
2. Terdapat kunci *lock* tipe *stage-2* untuk mahasiswa dan tahun akademik tersebut (`E_LOCKED`).

Request:
* `course_id`: *id* mata kuliah yang hendak diubah nilainya.
* `academic_year`: tahun akademik pengambilan mata kuliah.
* `score`: nilai mata kuliah.

```
ACCESS: student
PATCH /enrolments/<course_id>/academic-years/<academic_year>
{
    "course_id": "string|uuidv4", 
    "academic_year": "string|academic_year",
    "score": "string|score"
}
```

Success:
***Not Yet Specified***

Error:
* `E_INVALID_SETTINGS`: terdapat kesalahan pada konfigurasi *server*.
* `E_LOCKED`: pengubahan *enrolment* gagal karena terdapat kunci *stage-2* atau tahun akademik di mana mahasiswa hendak mengambil mata kuliah berbeda dengan tahun akademik yang aktif saat ini.
* `E_NOT_EXIST`: pengambilan mata kuliah tidak ditemukan.


### Menghapus Enrolment
Digunakan oleh mahasiswa untuk menghapus pengambilan mata kuliah.

Penghapusan pengambilan mata kuliah dapat gagal (diluar kesalahan biasa) karena dua alasan:
1. Pengambilan mata kuliah yang hendak dihapus memiliki tahun akademik berbeda dengan tahun akademik saat ini (`E_LOCKED`);
2. Terdapat kunci *lock* tipe *stage-1* untuk mahasiswa dan tahun akademik tersebut (`E_LOCKED`).

Request:
* `course_id`: *id* mata kuliah yang hendak diubah nilainya.
* `academic_year`: tahun akademik pengambilan mata kuliah.

```
ACCESS: student
DELETE /enrolments/<course_id>/academic-years/<academic_year>
{
    "course_id": "string|uuidv4",
    "academic_year": "integer|academic_year"
}
```

Success:
***Not Yet Specified***

Error:
* `E_INVALID_SETTINGS`: terdapat kesalahan pada konfigurasi *server*.
* `E_LOCKED`: penghapusan *enrolment* gagal karena terdapat kunci *stage-2*, atau tahun akademik di mana mahasiswa hendak mengambil mata kuliah berbeda dengan tahun akademik yang aktif saat ini.
* `E_NOT_EXIST`: pengambilan mata kuliah tidak ditemukan.

### Melihat Daftar Enrolment (Student)
Digunakan oleh mahasiswa untuk melihat daftar *enrolment* pada suatu tahun akademik. *Enrolment* yang akan dikembalikan adalah milik mahasiswa pemilik *token*.

Request:
* `academic_year`: tahun akademik *enrolment*.

```
ACCESS: student
DELETE /enrolments/academic-years/<academic_year>
{
    "academic_year": "integer|academic_year"
}
```

Success:
-

### Melihat Daftar Enrolment (Admin dan Lecturer)
Digunakan oleh pengguna dengan *role* *admin* atau *lecturer* untuk melihat daftar *enrolment* seorang mahasiswa pada suatu tahun akademik.

Request:
* `academic_year`: tahun akademik *enrolment*.
* `student_id`: *id* pengguna yang mereferensikan mahasiswa pemilik *enrolment*.

```
ACCESS: admin, lecturer
DELETE /enrolments/academic-years/<academic_year>/students/<student_id>
{
    "academic_year": "integer|academic_year",
    "student_id": "string|uuidv4"
}
```

Success:
***Not Yet Specified***

### Membuat Lock Enrolment
Request:
* `academic_year`: tahun akademik di mana *lock* akan dibuat.
* `stage`: *stage* dari *lock* yang akan dibuat.
* `students`: *array* dari *id* mahasiswa yang akan dikunci (*locking*).

```
ACCESS: admin
POST /enrolments/locks/create
{
    "academic_year": "integer|academic_year",
    "stage": "integer|range:1-2",
    "students": [
        "string|uuidv4"
    ]
}
```

Success:
***Not Yet Specified***

Error:
* `E_EXIST`: *lock* dengan tahun akademik dan *stage* yang sama serta untuk mahasiswa yang sama telah ada sebelumnya.
* `E_NOT_EXIST` (*field* `student_id`): salah satu mahasiswa yang direferensikan pada *array* `students` tidak ada.
* `E_NOT_EXIST` (*field* `academic_year`): tahun akademik tidak ditemukan.

### Menghapus Lock Enrolment
Request:
* `academic_year`: *lock* pada tahun akademik yang akan dihapus.
* `stage`: *stage* dari *lock* yang akan dihapus.
* `students`: *array* dari *id* mahasiswa.

```
ACCESS: admin
DELETE /enrolments/locks/delete
{
    "academic_year": "integer|academic_year",
    "stage": "integer|range:1-2",
    "students": [
        "string|uuidv4"
    ]
}
```

Success:
***Not Yet Specified***

Error:
* `E_NOT_EXIST`: *lock* tidak ditemukan