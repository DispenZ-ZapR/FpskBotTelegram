package com.example.fpskguidebot.service;

import com.example.fpskguidebot.enums.Language;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    public String getWelcomeMessage(Language language) {
        return switch (language) {
            case RU -> "Выберите интересующий вас раздел в меню ниже: 👇";
            case KG -> "Төмөндөгү менюдө кызыккан бөлүмүңүздү тандаңыз: 👇";
            case EN -> "Select the section you are interested in from the menu below: 👇";
        };
    }
    
    public String getLanguageSelectionMessage() {
        return "🌐 Пожалуйста, выберите язык / Тилди тандаңыз / Please select language:";
    }
    
    public String getStartMessage(Language language) {
        return switch (language) {
            case RU -> "Здравствуйте! Вы обратились в официальный бот Федерации профессиональных союзов Кыргызстана. 🇰🇬\n\n" +
                    "Мы здесь, чтобы защищать ваши трудовые права и помогать в профессиональном росте.\n\n" +
                    "«Профсоюзы — это ваша опора и защита в мире труда!»";
            case KG -> "Саламатсызбы! Сиз Кыргызстан профсоюздар федерациясынын расмий ботуна кайрылдыңыз. 🇰🇬\n\n" +
                    "Биз сиздин эмгек укуктарыңызды коргоо жана кесиптик өсүүгө жардам берүү үчүн бул жердебиз.\n\n" +
                    "«Профсоюздар — бул эмгек дүйнөсүндөгү сиздин таянычыңыз жана коргоочуңуз!»";
            case EN -> "Hello! You have contacted the official bot of the Federation of Trade Unions of Kyrgyzstan. 🇰🇬\n\n" +
                    "We are here to protect your labor rights and help in professional growth.\n\n" +
                    "«Trade unions are your support and protection in the world of work!»";
        };
    }
    
    public String getHelpMessage(Language language) {
        return switch (language) {
            case RU -> "📚 *Справка*\n\n" +
                    "Доступные команды:\n" +
                    "/start - Главное меню\n" +
                    "/help - Эта справка\n" +
                    "/lang - Сменить язык\n" +
                    "Используйте кнопки для навигации 👇";
            case KG -> "📚 *Жардам*\n\n" +
                    "Жеткиликтүү командалар:\n" +
                    "/start - Башкы меню\n" +
                    "/help - Бул жардам\n" +
                    "/lang - Тилди өзгөртүү\n" +
                    "Навигация үчүн баскычтарды колдонуңуз 👇";
            case EN -> "📚 *Help*\n\n" +
                    "Available commands:\n" +
                    "/start - Main menu\n" +
                    "/help - This help\n" +
                    "/lang - Change language\n" +
                    "Use buttons for navigation 👇";
        };
    }
    
    public String getAboutFederationMessage(Language language) {
        return switch (language) {
            case RU -> "ФПСК — это крупнейшее объединение, которое стоит на страже интересов трудящихся по всей стране.\n\n" +
                    "*Наши главные задачи*:\n\n" +
                    "⚖️ Защита прав: юридические консультации и помощь в спорах.\n\n" +
                    "🛡️ Безопасность: контроль условий труда на рабочих местах.\n\n" +
                    "🧘 Оздоровление: доступ к санаториям и домам отдыха.\n\n" +
                    "🎓 Развитие: обучение и повышение квалификации.\n\n" +
                    "Объединяем более 20 отраслевых профсоюзов!";
            case KG -> "ФПСК — бул бүт өлкө боюнча эмгекчилердин кызыкчылыктарын коргогон эң ири бирикме.\n\n" +
                    "*Биздин негизги милдеттерибиз*:\n\n" +
                    "⚖️ Укуктарды коргоо: юридикалык кеңештер жана талаш-тартыштарга жардам.\n\n" +
                    "🛡️ Коопсуздук: жумуш орундарындагы эмгек шарттарын көзөмөлдөө.\n\n" +
                    "🧘 Ден соолукту чыңдоо: санаторийлер жана эс алуу үйлөрүнө кирүү.\n\n" +
                    "🎓 Өнүктүрүү: окутуу жана квалификациясын жогорулатуу.\n\n" +
                    "Биз 20дан ашык тармактык кесиптик биримдиктерди бириктиребиз!";
            case EN -> "FPSK is the largest association that protects the interests of workers throughout the country.\n\n" +
                    "*Our main tasks*:\n\n" +
                    "⚖️ Rights protection: legal consultations and assistance in disputes.\n\n" +
                    "🛡️ Safety: control of working conditions at workplaces.\n\n" +
                    "🧘 Health improvement: access to sanatoriums and rest homes.\n\n" +
                    "🎓 Development: training and qualification improvement.\n\n" +
                    "We unite more than 20 industry trade unions!";
        };
    }
    
    public String getContactsMessage(Language language) {
        return switch (language) {
            case RU -> "<b>📞 Контакты:</b>\n\n" +
                    "🤝 Орг. отдел и связи: <code>+996312625753</code>\n" +
                    "📞 Горячая линия: <code>+1229</code>\n" +
                    "📠 Приемная председателя: <code>+996312613238</code>\n\n"+
                    "<b>📧 Эл. почта:</b>\n" +
                    "• info@fpsk.kg\n" +
                    "• office@fpsk.kg\n\n" +
                    "<b>📍 Адрес:</b>\n" +
                    "<a href=\"https://2gis.kg/bishkek/geo/15763234351121225?m=74.598484%2C42.876493%2F16\">720001, г. Бишкек, пр-т Чуй 207</a>";
            case KG -> "<b>📞 Байланыштар:</b>\n\n" +
                    "🤝 Уюштуруу бөлүмү: <code>+996312625753</code>\n" +
                    "📞 Тез номер: <code>+1229</code>\n" +
                    "📠 Төраганын кабылдамасы: <code>+996312613238</code>\n\n"+
                    "<b>📧 Эл. почта:</b>\n" +
                    "• info@fpsk.kg\n" +
                    "• office@fpsk.kg\n\n" +
                    "<b>📍 Дарек:</b>\n" +
                    "<a href=\"https://2gis.kg/bishkek/geo/15763234351121225?m=74.598484%2C42.876493%2F16\">720001, Бишкек шаары, Чүй проспекти 207</a>";
            case EN -> "<b>📞 Contacts:</b>\n\n" +
                    "🤝 Organizational and Communications Department: <code>+996312625753</code>\n" +
                    "📞 Hotline: <code>+1229</code>\n" +
                    "📠 The Chairperson's Office: <code>+996312613238</code>\n\n"+
                    "<b>📧 Email:</b>\n" +
                    "• info@fpsk.kg\n" +
                    "• office@fpsk.kg\n\n" +
                    "<b>📍 Address:</b>\n" +
                    "<a href=\"https://2gis." +
                    "kg/bishkek/geo/15763234351121225?m=74.598484%2C42.876493%2F16\">720001, Bishkek, Chui Avenue 207</a>";
        };
    }
    
    public String getNewsMessage(Language language) {
        return switch (language) {
            case RU -> "📰 *Новости Федерации профсоюзов Кыргызстана*\n\n" +
                    "Следите за последними новостями и событиями ФПСК на нашем официальном сайте";
            case KG -> "📰 *Кыргызстан профсоюздар федерациясынын жаңылыктары*\n\n" +
                    "ФПКнын акыркы жаңылыктарын жана окуяларын биздин расмий сайтыбыздан байкап туруңуз";
            case EN -> "📰 *News of the Federation of Trade Unions of Kyrgyzstan*\n\n" +
                    "Follow the latest news and events of FPSK on our official website";
        };
    }
    public String getStatement(Language language){
        return switch (language){
            case RU -> "Оставьте ваше обращение на этот номер снизу 👇";
            case KG -> "Төмөндөгү номерге кайрылууңузду калтырыңыз 👇";
            case EN -> "Please leave your message at the number below 👇";
        };
    }
    public String getUnknownCommandMessage(Language language) {
        return switch (language) {
            case RU -> "❓ Неизвестная команда\n\nИспользуйте /help для получения справки";
            case KG -> "❓ Белгисиз команда\n\nЖардам алуу үчүн /help колдонуңуз";
            case EN -> "❓ Unknown command\n\nUse /help for assistance";
        };
    }
    
    public String getRequestPromptMessage(Language language) {
        return switch (language) {
            case RU -> "✍️ Напишите ваше обращение:\n\n" +
                    "Пожалуйста, опишите вашу проблему или вопрос. " +
                    "Наш оператор рассмотрит его и ответит в ближайшее время.\n\n" +
                    "⚠️ Максимальная длина сообщения: 1000 символов";
            case KG -> "✍️ Кайрылууңузду жазыңыз:\n\n" +
                    "Сураныч, көйгөйүңүздү же суроонузду сүрөттөп бериңиз. " +
                    "Биздин оператор аны карап чыгып, жакынкы убакта жооп берет.\n\n" +
                    "⚠️ Билдирүүнүн максималдуу узундугу: 1000 символ";
            case EN -> "✍️ Write your request:\n\n" +
                    "Please describe your problem or question. " +
                    "Our operator will review it and respond shortly.\n\n" +
                    "⚠️ Maximum message length: 1000 characters";
        };
    }
    
    public String getRequestCancelledMessage(Language language) {
        return switch (language) {
            case RU -> "❌ Создание обращения отменено";
            case KG -> "❌ Кайрылуу түзүү жокко чыгарылды";
            case EN -> "❌ Request creation cancelled";
        };
    }
    
    public String getRequestConfirmationMessage(Language language, String messageText) {
        return switch (language) {
            case RU -> "✅ Ваше обращение принято!\n\n" +
                    "📝 Текст обращения: " + messageText + "\n\n" +
                    "Наш оператор свяжется с вами в ближайшее время. " +
                    "Пожалуйста, ожидайте ответа в этом чате.";
            case KG -> "✅ Сиздин кайрылууңуз кабыл алынды!\n\n" +
                    "📝 Кайрылуунун тексти: " + messageText + "\n\n" +
                    "Биздин оператор сиз менен жакынкы убакта байланышат. " +
                    "Сураныч, бул чатта жоопту күтүңүз.";
            case EN -> "✅ Your request has been accepted!\n\n" +
                    "📝 Request text: " + messageText + "\n\n" +
                    "Our operator will contact you shortly. " +
                    "Please wait for a response in this chat.";
        };
    }
    
    public String getRequestTooLongMessage(Language language, int maxLength) {
        return switch (language) {
            case RU -> "⚠️ Ваше сообщение слишком длинное!\n\n" +
                    "Максимальная длина: " + maxLength + " символов\n" +
                    "Ваше сообщение: " + "символов\n\n" +
                    "Пожалуйста, сократите текст и отправьте снова.";
            case KG -> "⚠️ Сиздин билдирүүңүз өтө узун!\n\n" +
                    "Максималдуу узундук: " + maxLength + " символ\n" +
                    "Сиздин билдирүү: " + "символ\n\n" +
                    "Сураныч, текстти кыскартып, кайра жөнөтүңүз.";
            case EN -> "⚠️ Your message is too long!\n\n" +
                    "Maximum length: " + maxLength + " characters\n" +
                    "Your message: " + "characters\n\n" +
                    "Please shorten the text and send again.";
        };
    }
    
    public String getRateLimitMessage(Language language, int maxRequests, int hours) {
        return switch (language) {
            case RU -> "⚠️ Превышен лимит обращений!\n\n" +
                    "Вы можете отправить не более " + maxRequests + " обращений в течение " + hours + " часов.\n" +
                    "Пожалуйста, дождитесь ответа на предыдущие обращения.";
            case KG -> "⚠️ Кайрылуулардын чеги ашты!\n\n" +
                    "Сиз " + hours + " саат ичинде " + maxRequests + " кайрылуудан көп жөнөтө албайсыз.\n" +
                    "Сураныч, мурунку кайрылууларга жоопту күтүңүз.";
            case EN -> "⚠️ Request limit exceeded!\n\n" +
                    "You can send no more than " + maxRequests + " requests within " + hours + " hours.\n" +
                    "Please wait for responses to previous requests.";
        };
    }
    
    public String getOperatorResponseMessage(Language language, String responseText) {
        return switch (language) {
            case RU -> "💬 Ответ оператора:\n\n" + responseText + 
                    "\n\n---\nЕсли у вас есть еще вопросы, нажмите '📞 Обращение' снова.";
            case KG -> "💬 Оператордун жообу:\n\n" + responseText + 
                    "\n\n---\nЭгер дагы суроолоруңуз болсо, '📞 Кайрылуу' баскычын кайра басыңыз.";
            case EN -> "💬 Operator response:\n\n" + responseText + 
                    "\n\n---\nIf you have more questions, press '📞 Statement' again.";
        };
    }
    
    public String getCancelButtonText(Language language) {
        return switch (language) {
            case RU -> "❌ Отменить";
            case KG -> "❌ Жокко чыгаруу";
            case EN -> "❌ Cancel";
        };
    }
    
    public String[] getMenuButtons(Language language) {
        return switch (language) {
            case RU -> new String[]{"📖 О Федерации", "ℹ️ Контакты", "📰 Новости","📞 Обращение"};
            case KG -> new String[]{"📖 Федерация жөнүндө", "ℹ️ Байланыштар", "📰 Жаңылыктар","📞 Кайрылуу"};
            case EN -> new String[]{"📖 About Federation", "ℹ️ Contacts", "📰 News","📞 Statement"};
        };
    }
    public String getNewsSite(Language language){
        return switch (language){
            case RU -> "https://fpsk.kg/%d0%bd%d0%be%d0%b2%d0%be%d1%81%d1%82%d0%b8/";
            case EN -> "https://fpsk.kg/en/news/";
            case KG -> "https://fpsk.kg/ky/%d0%b6%d0%b0%d2%a3%d1%8b%d0%bb%d1%8b%d0%ba%d1%82%d0%b0%d1%80/";
        };
    }
    public String getLeadershipButtonText(Language language) {
        return switch (language) {
            case RU -> "📋 Руководство ФПСК и прием граждан";
            case KG -> "📋 ФПСК жетекчилиги жана жарандарды кабыл алуу";
            case EN -> "📋 FPSK Leadership and Citizen Reception";
        };
    }
    public String getSite(Language language){
        return switch (language){
            case RU -> "Наш сайт";
            case KG -> "Биздин сайт";
            case EN -> "Our website";
        };
    }


    public String getSiteUrl(Language language){
        return switch (language){
            case RU -> "https://fpsk.kg/";
            case KG -> "https://fpsk.kg/ky/bashky-bet/";
            case EN -> "https://fpsk.kg/en/home-page/";
        };
    }
    public String getNewsButtonText(Language language) {
        return switch (language) {
            case RU -> "🌐 Перейти на сайт с новостями";
            case KG -> "🌐 Жаңылыктар менен сайтка өтүү";
            case EN -> "🌐 Go to news website";
        };
    }
    public String getLeadershipMessage(Language language) {
        return switch (language) {
            case RU -> "🏛 *Руководство ФПСК*\n\n" +
                    "👤 *Джумадилде уулу Мурадил*\n" +
                    "_Председатель Федерации_\n" +
                    "📞 Приемная: `+996312613238` \n" +
                    "🗓 Приемные дни: *Вторник* 11:00–13:00\n" +
                    "🚪 Кабинет: *401*\n" +
                    "————————————————\n" +
                    "👤 *Жантелиев Канат Талантович*\n" +
                    "_Первый заместитель председателя_\n" +
                    "📞 Контактный тел: `+996312613285` \n" +
                    "🗓 Приемные дни: *Среда* 11:00–13:00\n" +
                    "🚪 Кабинет: *404*\n" +
                    "————————————————\n" +
                    "👤 *Солтонбекова Ализа Карыпбековна*\n" +
                    "_Заместитель председателя_\n" +
                    "📞 Контактный тел: `+996312613279` \n" +
                    "🗓 Приемные дни: *Четверг* 11:00–12:00\n" +
                    "🚪 Кабинет: *404*";
                    
            case KG -> "🏛 *ФПСК жетекчилиги*\n\n" +
                    "👤 *Джумадилде уулу Мурадил*\n" +
                    "_Федерация төрагасы_\n" +
                    "📞 Кабылдама: `+996312613238` \n" +
                    "🗓 Кабыл алуу күндөрү: *Шейшемби* 11:00–13:00\n" +
                    "🚪 Кабинет: *401*\n" +
                    "————————————————\n" +
                    "👤 *Жантелиев Канат Талантович*\n" +
                    "_Төраганын биринчи орунбасары_\n" +
                    "📞 Байланыш тел: `+996312613285` \n" +
                    "🗓 Кабыл алуу күндөрү: *Шаршемби* 11:00–13:00\n" +
                    "🚪 Кабинет: *404*\n" +
                    "————————————————\n" +
                    "👤 *Солтонбекова Ализа Карыпбековна*\n" +
                    "_Төраганын орунбасары_\n" +
                    "📞 Байланыш тел: `+996312613279` \n" +
                    "🗓 Кабыл алуу күндөрү: *Бейшемби* 11:00–12:00\n" +
                    "🚪 Кабинет: *404*";
                    
            case EN -> "🏛 *FPSK Leadership*\n\n" +
                    "👤 *Djumadilde uulu Muradil*\n" +
                    "_Chairman of the Federation_\n" +
                    "📞 Reception: `+996312613238` \n" +
                    "🗓 Reception days: *Tuesday* 11:00–13:00\n" +
                    "🚪 Office: *401*\n" +
                    "————————————————\n" +
                    "👤 *Zhanteliev Kanat Talantovich*\n" +
                    "_First Deputy Chairman_\n" +
                    "📞 contact number: `+996312613285` \n" +
                    "🗓 Reception days: *Wednesday* 11:00–13:00\n" +
                    "🚪 Office: *404*\n" +
                    "————————————————\n" +
                    "👤 *Soltonbekova Aliza Karypbekovna*\n" +
                    "_Deputy Chairman_\n" +
                    "📞 contact number: `+996312613279` \n" +
                    "🗓 Reception days: *Thursday* 11:00–12:00\n" +
                    "🚪 Office: *404*";
        };
    }
}
