package com.example.weatherapp.settings.viewmodel

//class SettingsViewModel(private val weatherRepository: WeatherRepository, private val context: Context): ViewModel() {
//
//    var setReturn = weatherRepository.getSettings()
//    lateinit var set : Settings
//
//    fun insertSettings(settings: Settings) {
//        CoroutineScope(Dispatchers.IO).launch {
//            weatherRepository.insertSettings(settings)
//        }
//    }
//
//    fun updateSettings(settings: Settings) {
//        CoroutineScope(Dispatchers.IO).launch {
//            weatherRepository.updateSettings(settings)
//        }
//    }
//
//    fun getSettings(): Settings {
//        CoroutineScope(Dispatchers.IO).launch {
//            set=weatherRepository.getSettings()
//        }
//        return set
//    }
//}