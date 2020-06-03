package com.bifexey.simpleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EachFAQ extends AppCompatActivity {

    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();

    int index;

    TextView e_FAQ_question, e_FAQ_answer;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_faq);

        index = getIntent().getIntExtra("index", 0);

        questions.add("Зачем нужны статусы “ученик” и “преподаватель” и как они работают?");
        questions.add("Что такое SOS ( запросы о быстрой помощи )?");
        questions.add("Зачем в разделе SOS указывать свой корпус?");
        questions.add("Как найти репетитора?");
        questions.add("Как работает “SOS” ( запросы о быстрой помощи ), если я в статусе “ученик”?");
        questions.add("Как работает SOS, если я в статусе “преподавателя”?");
        questions.add("Как оформить подписку?");
        questions.add("Как отменить подписку?");
        questions.add("Почему меняется количество кнопок меню быстрого доступа при смене статуса?");
        questions.add("Нужен ли преподавательский опыт для регистрации в качестве преподавателя?");
        questions.add("Как создать репетиторское объявление?");
        questions.add("Как происходит сделка между учеником и репетитором?");
        questions.add("Зачем указывать корпус при создании репетиторского объявления?");
        questions.add("Может ли приложение предоставить мою личную информацию сторонним лицам?");
        questions.add("Как много я могу заработать за месяц?");

        answers.add("У вас в профиле есть две кнопки для смены статуса: “ученик” и “преподаватель”? Когда вы только регистрируетесь в приложении, вы находитесь в статусе “ученика”. Данный статус позволяет вам искать и связываться с репетиторами в меню поиска и оставлять заявки на быструю помощь (“SOS”). Если вы попытаетесь сменить статус, нажав на кнопку “преподаватель”, то приложение предложит вам оформить подписку. После оформления подписки, ваш статус меняется на статус “преподаватель”, в меню быстрого доступа пропадает кнопка “SOS”, а меню поиска теперь показывает запросы о быстрой помощи (“SOS”) от “учеников”. Данный статус позволит вам оказывать репетиторские услуги за денежное вознаграждение другим пользователям. Для этого вам в профиле надо будет создать объявление, указав в каком предмете вы специализируетесь и другую уточняющую информацию. Также статус “преподаватель” позволяет вам оказывать быструю, единовременную помощь по какой-то конкретной теме “ученикам” за денежное вознаграждение. Список “учеников”, нуждающихся в помощи, вы можете найти в меню поиска.\n" +
                "Между статусами можно будет переключаться в любой момент, в зависимости от того, что вам нужно: получить или оказать помощь.");
        answers.add("“SOS” - это функция, позволяющая “ученикам” размещать запросы о быстрой, единовременной помощи. Например, вы не поняли какую-то конкретную тему по высшей математике, вы заходите в раздел “SOS” и создаете запрос. Данные запросы отличаются тем, что вы выбираете, когда вам удобно будет провести занятие. ( Например: Сегодня в 15:40) Подобные занятия являются единовременными и, как правило, проводятся в корпусе, который вы указали либо неподалеку. Вы создаете запросы о помощи только тогда, когда она вам нужна. Также вы сами назначаете цену, которую готовы заплатить за час оказания услуг.");
        answers.add("Это функция опциона. Вы можете указать свой корпус, если бы вы хотели провести занятие в корпусе, где вы обучаетесь или неподалеку.");
        answers.add("Чтобы найти репетитора, нажмите на кнопку “Поиск” в меню быстрого доступа. Перед вами появится экран с предметами. Выберите тот предмет, по которому нуждаетесь в помощи. Затем появится экран, где вы можете указать раздел предмета и место, где бы вам было удобно заниматься. Также вы можете выбрать дни по которым вам было бы удобно заниматься и установить ценовой диапазон. После этого нажмите внизу экрана кнопку “поиск”, и перед вами появится список репетиторов, подобранных по критериям, которые вы указали.");
        answers.add("Если вы находитесь в статусе “ученик”, то нажав кнопку “SOS” в меню быстрого доступа, у вас откроется этот раздел. В этом разделе размещаются актуальные, созданные вами запросы о быстрой помощи (“SOS”). Внизу экрана вы увидите кнопку “Разместить запрос о быстрой помощи”. Нажав на эту кнопку у вас откроется экран, где вам нужно будет выбрать предмет из выпадающего списка, в котором вам нужна помощь, то как этот предмет называется у вас на программе, а также конкретную тему, по которой вам нужна помощь (Например, предмет: “математика”; название предмета на программе: “линейная алгебра”; тема: “обратные матрицы”). Далее вам потребуется выбрать корпус университета, в котором вы находитесь, дату и время, когда вам удобно будет заниматься. Далее, в разделе “цена”, вы должны указать сумму, которую вы готовы заплатить репетитору за оказание помощи. После этого шага вы можете написать комментарий, в котором можете более подробно описать то, в чем вам нужна помощь, место, где вам было бы удобнее провести занятие и любую другую уточняющую информацию. После заполнения всей необходимой информации, нажмите кнопку разместить, и ваше объявление будет видно всем репетиторам. Объявление удаляется автоматически в тот момент, когда истекает дата, которую вы указали в объявлении.");
        answers.add("Если, будучи в статусе “преподавателя”, вы нажмете кнопку “SOS” в меню быстрого доступа, то вам откроется список всех запросов о быстрой помощи, которые разместили “ученики”. Нажав на какой-то запрос, вам откроется более подробная информация об этом запросе. Если вас все устраивает, то внизу экрана нажмите на кнопку написать, и вас перенесет в чат с “учеником”, который создавал этот запрос. В чате вы можете уточнить информацию и договориться о занятии.");
        answers.add("Подписку можно оформить нажав, на статус “преподаватель”. Приложение откроет экран с разными планами. Выберите подходящий вам и вас перенес в окно оплаты.");
        answers.add("Подписку можно отменить в настройках. Иконка настроек находится в профиле, сверху, с правой стороны от вашего аватара.");
        answers.add("Количество кнопок меняется, потому что у “ученика” и “преподавателя” разные задачи. “Ученику” доступны функции “SOS” (“Разместить запрос о быстрой помощи”) И “Поиск” репетитора, а у “преподавателя” “Поиск” меняется на поиск по запросам о быстрой помощи(“SOS”), которые разместили “ученики”.");
        answers.add("Нет, опыт не обязателен. Однако, если он вас есть, то вы можете указать это в комментарии о себе при создании репетиторского объявления.");
        answers.add("Для того, чтобы создать объявления, войдите в ваш профиль. Под статусами вы увидите кнопку “создать объявление” и “мои объявления”. Нажмите на кнопку “создать объявление”, после чего перед вами появится экран, где вам нужно будет заполнить информацию о себе. Затем нажмите кнопку “разместить объявление” внизу экрана. Теперь ваше объявление видно всем “ученикам”. Вы можете просматривать, редактировать и удалять ваши объявления в профиле, нажав кнопку “мои объявления”.");
        answers.add("После того, как вы нашли нужного вам репетитора или ученика, свяжитесь с ним в чате. Обсудите все детали еще раз. О том, как ученик оплачивает услуги репетитора, участники сделки договариваются между собой.");
        answers.add("Это нужно для того, чтобы студенты при выборе репетитора примерно понимали, где территориально находится репетитор. Это информация нужна студенту в том случае, если он планирует заниматься сразу после занятий, неподалеку от корпуса, где он сам учится. В таком случае студенту будет удобнее, если его репетитор будет учится в его же корпусе или ближайшем от него.");
        answers.add("Нет, это исключено. Приложение не может распространять информацию о своих пользователях. Если желаете подробнее ознакомится с политикой конфиденциальности приложения, то перейдите в раздел “о приложении” в вашем профиле, а затем в раздел “политика конфиденциальности”.");
        answers.add("В этом вопросе все зависит от вас и цен, которые вы установили за ваши услуги. В среднем репетиторы со стажем по Москве берут от 1000 до 1500 за час. Если вы установите ценник, например, в 700 рублей в час и будете заниматься со студентами хотя бы 2 часа в день, то заработаете целых 42000 за месяц, а это еще без учета того, сколько вы можете заработать оказывая быструю помощь в разделе “SOS”!  Звучит неплохо, не так ли!");

        e_FAQ_question = findViewById(R.id.e_FAQ_question);
        e_FAQ_answer = findViewById(R.id.e_FAQ_answer);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        e_FAQ_question.setText(questions.get(index));
        e_FAQ_answer.setText(answers.get(index));
    }
}
