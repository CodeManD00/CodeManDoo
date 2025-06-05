document.getElementById("submitButton").addEventListener("click", async function () {
    const form = document.getElementById("promptForm");

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const castInput = document.getElementById("cast").value.split(",").map(name => name.trim());

    const data = {
        title: document.getElementById("title").value,
        location: document.getElementById("location").value,
        date: document.getElementById("date").value,
        genre: document.getElementById("genre").value,
        cast: castInput,
        review: document.getElementById("review").value
    };

    try {
        const response = await fetch("http://localhost:8080/generate-image", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        localStorage.setItem("generatedPrompt", result.prompt);
        localStorage.setItem("generatedImageUrl", result.imageUrl);
        window.location.href = "/result.html";

    } catch (error) {
        alert("에러가 발생했습니다: " + error.message);
    }
});
