/* =========================================================
   PEPITO CHRONICLE - Interactive UX Engine (Vanilla JS)
   ========================================================= */

document.addEventListener('DOMContentLoaded', () => {

    // -------------------------------------------------------------
    // 1. CALCOLO AUTOMATICO TEMPO DI LETTURA (Detail & Cards)
    // -------------------------------------------------------------
    const articleBody = document.querySelector('.article-content-body');
    if (articleBody) {
        const text = articleBody.innerText || '';
        const wordCount = text.trim().split(/\s+/).length;
        const readTimeMinutes = Math.max(1, Math.ceil(wordCount / 200));

        const metaContainer = document.querySelector('.article-hero-header .container');
        if (metaContainer) {
            const timeBadge = document.createElement('span');
            timeBadge.className = 'badge rounded-pill bg-light text-dark border px-3 py-2 ms-2 align-middle';
            timeBadge.innerHTML = `<i class="fa-regular fa-clock text-warning me-1"></i> ${readTimeMinutes} min di lettura`;
            const headerTarget = metaContainer.querySelector('.d-flex');
            if (headerTarget) headerTarget.appendChild(timeBadge);
        }
    }


    // -------------------------------------------------------------
    // 2. ZEN READING TOOLBAR & TEXT-TO-SPEECH (Detail Article)
    // -------------------------------------------------------------
    if (articleBody) {
        const toolbar = document.createElement('div');
        toolbar.id = 'zen-toolbar';
        toolbar.className = 'zen-toolbar-container shadow-sm';
        toolbar.innerHTML = `
            <div class="d-flex align-items-center gap-2 bg-white border p-2 rounded-pill shadow-sm">
                <button id="btn-font-dec" class="btn btn-sm btn-light rounded-circle" title="Riduci font">A-</button>
                <button id="btn-font-inc" class="btn btn-sm btn-light rounded-circle" title="Ingrandisci font">A+</button>
                <div class="vr my-1"></div>
                <button id="btn-zen-mode" class="btn btn-sm btn-light rounded-pill px-3" title="Attiva modalità riposo">
                    <i class="fa-solid fa-mug-saucer me-1 text-danger"></i> Modalità Zen
                </button>
                <div class="vr my-1"></div>
                <button id="btn-tts" class="btn btn-sm btn-sunset rounded-pill px-3 text-white">
                    <i class="fa-solid fa-volume-high me-1"></i> Ascolta
                </button>
            </div>
        `;

        articleBody.parentNode.insertBefore(toolbar, articleBody);

        // Regolazione dinamica dimensione font
        let currentFontSize = 1.15; // rem
        document.getElementById('btn-font-inc').addEventListener('click', () => {
            if (currentFontSize < 1.6) {
                currentFontSize += 0.1;
                articleBody.style.fontSize = currentFontSize + 'rem';
            }
        });

        document.getElementById('btn-font-dec').addEventListener('click', () => {
            if (currentFontSize > 0.9) {
                currentFontSize -= 0.1;
                articleBody.style.fontSize = currentFontSize + 'rem';
            }
        });

        // Modalità Zen: Tonalità morbide e riposanti (Warm Paper)
        let zenActive = false;
        document.getElementById('btn-zen-mode').addEventListener('click', function() {
            zenActive = !zenActive;
            if (zenActive) {
                document.body.style.backgroundColor = '#fbf7ee';
                articleBody.style.color = '#2c251e';
                articleBody.style.lineHeight = '2.1';
                this.classList.add('btn-warning');
                this.innerHTML = '<i class="fa-solid fa-check me-1"></i> Zen Attiva';
            } else {
                document.body.style.backgroundColor = '';
                articleBody.style.color = '';
                articleBody.style.lineHeight = '';
                this.classList.remove('btn-warning');
                this.innerHTML = '<i class="fa-solid fa-mug-saucer me-1 text-danger"></i> Modalità Zen';
            }
        });

        // Text-to-Speech nativo
        let isSpeaking = false;
        const synth = window.speechSynthesis;
        const ttsBtn = document.getElementById('btn-tts');

        ttsBtn.addEventListener('click', () => {
            if (!synth) {
                alert('La sintesi vocale non è supportata dal tuo browser.');
                return;
            }

            if (isSpeaking) {
                synth.cancel();
                isSpeaking = false;
                ttsBtn.innerHTML = '<i class="fa-solid fa-volume-high me-1"></i> Ascolta';
            } else {
                const utterance = new SpeechSynthesisUtterance(articleBody.innerText);
                utterance.lang = 'it-IT';
                utterance.rate = 0.95; // ritmo calmo e naturale

                utterance.onend = () => {
                    isSpeaking = false;
                    ttsBtn.innerHTML = '<i class="fa-solid fa-volume-high me-1"></i> Ascolta';
                };

                synth.speak(utterance);
                isSpeaking = true;
                ttsBtn.innerHTML = '<i class="fa-solid fa-circle-stop me-1"></i> Interrompi';
            }
        });
    }


    // -------------------------------------------------------------
    // 3. LIVE SEARCH ISTANTANEA (Filtra le card mentre scrivi)
    // -------------------------------------------------------------
    const searchInput = document.querySelector('input[name="keyword"]');
    const articleCards = document.querySelectorAll('.article-card');

    if (searchInput && articleCards.length > 0) {
        searchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();

            articleCards.forEach(card => {
                const title = card.querySelector('.article-title')?.innerText.toLowerCase() || '';
                const subtitle = card.querySelector('.article-subtitle')?.innerText.toLowerCase() || '';
                const author = card.querySelector('.author-pill')?.innerText.toLowerCase() || '';

                const matches = title.includes(query) || subtitle.includes(query) || author.includes(query);
                const colParent = card.closest('.col-12, .col-md-6, .col-lg-4');

                if (colParent) {
                    colParent.style.display = matches ? '' : 'none';
                    if (matches && query.length > 0) {
                        card.style.animation = 'scaleIn 0.3s ease-out';
                    }
                }
            });
        });
    }


    // -------------------------------------------------------------
    // 4. LIVE IMAGE PREVIEW CON DRAG & DROP (Form Create ed Edit)
    // -------------------------------------------------------------
    const fileInputs = document.querySelectorAll('input[type="file"][name="file"]');
    fileInputs.forEach(input => {
        const parentBox = input.closest('div');
        if (!parentBox) return;

        // Contenitore preview dinamico
        const previewContainer = document.createElement('div');
        previewContainer.className = 'mt-3 text-center d-none';
        previewContainer.innerHTML = `
            <img class="img-preview-dynamic shadow-sm rounded-4 border" style="max-height: 220px; width: auto; object-fit: cover;" alt="Anteprima">
            <div class="small text-muted mt-1"><i class="fa-solid fa-eye me-1"></i> Anteprima immagine caricata</div>
        `;
        parentBox.appendChild(previewContainer);

        input.addEventListener('change', function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const img = previewContainer.querySelector('img');
                    img.src = e.target.result;
                    previewContainer.classList.remove('d-none');
                };
                reader.readAsDataURL(file);
            }
        });
    });


    // -------------------------------------------------------------
    // 5. TOAST NOTIFICHE AUTO-DISTRUGGENTI
    // -------------------------------------------------------------
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        // Barra di countdown progressivo
        const bar = document.createElement('div');
        bar.className = 'alert-progress-bar';
        alert.appendChild(bar);
        alert.style.position = 'relative';
        alert.style.overflow = 'hidden';

        setTimeout(() => {
            alert.style.transition = 'all 0.5s ease-out';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });

});