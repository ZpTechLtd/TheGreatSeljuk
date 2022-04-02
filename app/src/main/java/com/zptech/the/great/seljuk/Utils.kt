package com.zptech.the.great.seljuk

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zptech.the.great.seljuk.model.RingTone
import java.util.*

fun getList(): MutableList<RingTone> {

    val list: MutableList<RingTone> = ArrayList()

    list.add(
        RingTone(
            "Asmaul Husna",
            R.raw.asmaul_husna,
            false,
            "3:32",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "Muhammad Nabina",
            R.raw.muhammad_nabina,
            false,
            "0:24",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Wo Mera Nabi Mera Nabi",
            R.raw.wo_mera_nabi_mera_nabi,
            false,
            "0:59",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Chal Deen Ki Tableegh mein",
            R.raw.chal_deen_ki_tableegh_mein,
            false,
            "0:53",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Noor E Ramzaan",
            R.raw.noor_e_ramzan,
            false,
            "0:28",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "Kerlo Ishq Muhammad Naal",
            R.raw.kerlo_ishq_muhammad_naal,
            false,
            "0:36",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Muskan Allah Hu Akbar",
            R.raw.muskan_allah_hu_akbar,
            false,
            "0:31",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Muskan Allah Hu Akbar2",
            R.raw.muskan_allah_hu_akbar_two,
            false,
            "0:26",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "O Allah The Almighty",
            R.raw.o_allah_the_almighty,
            false,
            "0:19",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Sehar ka Waqt Tha",
            R.raw.sehar_ka_waqt_tha,
            false,
            "0:51",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "Ya makka",
            R.raw.ya_makka,
            false,
            "0:25",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "Beautiful Dua Ringtone",
            R.raw.beautiful_dua_ringtone,
            false,
            "0:34",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "Heart Touching Ringtone",
            R.raw.heart_islamic_ringtone,
            false,
            "0:36",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "New Salaam Ringtone",
            R.raw.new_salam_ringtone,
            false,
            "0:30",
            R.drawable.gradient_one
        )
    )

    list.add(
        RingTone(
            "Allah Allah Naat",
            R.raw.allah_allah_naat,
            false,
            "00:19",
            R.drawable.gradient_one
        )
    )
    list.add(
        RingTone(
            "Allah Hu Allah Hu",
            R.raw.allah_hu_allah_hu,
            false,
            "00:20", R.drawable.gradient_two
        )
    )
    list.add(
        RingTone(
            "Fajr",
            R.raw.assalatu_khairum_minannaum,
            false,
            "00:28",
            R.drawable.gradient_three
        )
    )
    list.add(
        RingTone(
            "Bismillah",
            R.raw.bismillah_ir_rahman,
            false,
            "00:09", R.drawable.gradient_four
        )
    )
    list.add(
        RingTone(
            "Dil Mein Ishq e Nabi",
            R.raw.dil_mein_ishqe_nabi,
            false,
            "00:29", R.drawable.gradient_five
        )
    )
    list.add(
        RingTone(
            "Ya Rabb",
            R.raw.islamic_ia_rab,
            false,
            "00:29", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "La Illaha Illallah",
            R.raw.la_illaha_illallah,
            false,
            "00:28", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Meri Qismat Jagane Ko",
            R.raw.meri_qismat_jagane_ko,
            false,
            "00:27", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Ramzan Music",
            R.raw.ramzan_music,
            false,
            "00:12", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Ya Makkah",
            R.raw.ya_makkah,
            false,
            "00:25", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Ya Rabbal Alameen",
            R.raw.ya_rabbal_alameen,
            false,
            "00:48", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Ya Rasool",
            R.raw.ya_rasool,
            false,
            "00:28", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Ya Taiba",
            R.raw.ya_taiba,
            false,
            "00:28", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Ya Wajiha",
            R.raw.ya_wajiha,
            false,
            "00:19", R.drawable.gradient_six
        )
    )
    list.add(
        RingTone(
            "Heart Touching",
            R.raw.heart_touching_ringtones,
            false,
            "00:15", R.drawable.gradient_six
        )
    )
    list.add(
        RingTone(
            "Ahmad Rasool",
            R.raw.ahmad_rasool,
            false,
            "00:28", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Allah Humma Sallay Ala",
            R.raw.allahumma_sallay_ala_2,
            false,
            "00:18", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Dawate Islami",
            R.raw.dawate_islami,
            false,
            "00:28", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Labbaik Allahuma Labbaik",
            R.raw.labbaik_allahuma,
            false,
            "00:30", R.drawable.gradient_seven
        )
    )
    list.add(
        RingTone(
            "Makkah Azaan",
            R.raw.makkah_azaan,
            false,
            "00:29", R.drawable.gradient_eight
        )
    )
    list.add(
        RingTone(
            "Maula Ya Salli",
            R.raw.maula_ya_salli,
            false,
            "00:25", R.drawable.gradient_nine
        )
    )
    list.add(
        RingTone(
            "Maula Ya Salli Flute",
            R.raw.maula_ya_salli_flute,
            false,
            "00:18", R.drawable.gradient_ten
        )
    )
    list.add(
        RingTone(
            "Muhammad Nabeena",
            R.raw.muhammad_nabeena,
            false,
            "00:29", R.drawable.gradient_eleven
        )
    )

    list.add(
        RingTone(
            "Hasbi Rabbi Jallallah",
            R.raw.hasbi_rabbi_jallallah,
            false,
            "00:33", R.drawable.gradient_eleven
        )
    )

    list.add(
        RingTone(
            "I Am A Muslim",
            R.raw.i_am_a_muslim,
            false,
            "00:20", R.drawable.gradient_eleven
        )
    )

    list.add(
        RingTone(
            "Ya Rasool Salmalika",
            R.raw.ya_rasool_salmalika,
            false,
            "00:16", R.drawable.gradient_twelve
        )
    )
    list.add(
        RingTone(
            "Islam",
            R.raw.islam,
            false,
            "00:29", R.drawable.gradient_six
        )
    )

    list.add(
        RingTone(
            "Jamalul Wojude",
            R.raw.jamalul_wojoode,
            false,
            "00:27", R.drawable.gradient_six
        )
    )


    return list
}

fun isPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestPermission(context: Activity) {
    ActivityCompat.requestPermissions(
        context,
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
        12
    )
}