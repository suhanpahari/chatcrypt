const API_URL = "http://localhost:8080/api/messages";
const ENCRYPTION_KEY = "your-encryption-key";
const senderInp = document.getElementById("sender");

const url = 'http://localhost:8080';
let username = '';


async function handleAuthCheck() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.replace('/signin.html');
        return;
    }
}
handleAuthCheck()

async function addChattingWith(data) {
    if (data) {
        document.getElementById("chatting-with").innerHTML = ''
        data.forEach((user) => {
            document.getElementById("chatting-with").innerHTML += generateHtml(user)
        });
    }
}

async function getChattingWith() {
    const userEmail = localStorage.getItem('email');
    const res = await fetch(`${url}/chat/contacts?mainUserEmail=${userEmail}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    });
    const data = await res.json();
    addChattingWith(data);
}

getChattingWith();



let senderName = localStorage.getItem("email");

async function loadMessages() {
    const receiver = localStorage.getItem('recipient');
    const sender = localStorage.getItem('email');
    const response = await fetch(`${API_URL}?receiver=${receiver}&sender=${sender}`);
    const messages = await response.json();

    document.getElementById("messages").innerHTML = messages.map(msg => {
        const isCurrentUser = senderName === msg.sender;
        const decryptedContent = CryptoJS.AES.decrypt(msg.encryptedContent, ENCRYPTION_KEY).toString(CryptoJS.enc.Utf8);
        return `<p class='max-w-xs lg:max-w-md px-4 py-2 rounded-lg  ${isCurrentUser ? "self-end bg-blue-600 text-white" : "self-start bg-gray-200 text-gray-800"}' ><strong></strong> ${decryptedContent}</p>`;
    }).join("");
}

async function sendMessage() {
    const receiver = localStorage.getItem('recipient');
    const sender = localStorage.getItem('email');
    const messageText = document.getElementById("message").value;

    if (!senderName || !messageText) return alert("Please enter your name and message.");

    const encryptedContent = CryptoJS.AES.encrypt(messageText, ENCRYPTION_KEY).toString();

    const message = {
        sender: sender,
        encryptedContent: encryptedContent,
        receiver: receiver
    };

    await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(message)
    });

    document.getElementById("message").value = "";
    loadMessages();
}


setInterval(loadMessages, 1000);