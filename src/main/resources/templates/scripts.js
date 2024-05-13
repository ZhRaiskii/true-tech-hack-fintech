// Проверяем поддержку Web Speech API
if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
    // Создаем новый экземпляр объекта распознавания речи
    let recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();

    // Настройка параметров распознавания речи
    recognition.lang = 'ru-RU'; // Устанавливаем язык для распознавания (в данном случае, русский)

    // Обработчик, вызываемый при получении результатов распознавания
    recognition.onresult = function(event) {
        let text = event.results[0][0].transcript; // Получаем распознанный текст
        // Выводим распознанный текст на экран
        document.getElementById('speechResult').innerText = 'Вы сказали: ' + text;
        sendToServer(text);
    };

    // Обработчик ошибок распознавания речи
    recognition.onerror = function(event) {
        console.error('Произошла ошибка при распознавании речи:', event.error);
    };

    // Запускаем распознавание речи при нажатии на клавишу или клике мышью
    document.addEventListener('mousedown', function() {
        recognition.start();
    });
} else {
    console.error('Ваш браузер не поддерживает распознавание речи.');
}

function sendToServer(text) {

    $.ajax({
        type: 'POST',
        url: '/',
        data: { text: text },
        success: function(response) {
            // Получаем текущее содержимое диалогового окна
            let dialogueBox = document.getElementById('dialogueBox');
            // Создаем новый элемент для сообщения от сервера
            let message = document.createElement('div');
            message.textContent = response;
            // Добавляем сообщение в диалоговое окно
            dialogueBox.appendChild(message);
            // Прокручиваем диалоговое окно вниз, чтобы показать последнее сообщение
            dialogueBox.scrollTop = dialogueBox.scrollHeight;
            console.log('Сервер ответил: ', response);
        },
        error: function(xhr, status, error) {
            // Обработка ошибки
            console.error('Произошла ошибка при отправке на сервер:', status, error);
        }
    });
}