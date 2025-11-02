# 🚛 **Ei Truck — Aplicativo Mobile**

> Gerencie sua frota, viagens, tratativas e desempenho de motoristas de forma inteligente e integrada.

![Banner Ei Truck](https://i.imgur.com/1b9r3nX.png)
*(Adicione aqui uma imagem ou banner do app — ex: tela inicial ou dashboard)*

---

## 📱 **Visão Geral**

O **Ei Truck Mobile** é um aplicativo Android desenvolvido para apoiar **gestores de frota e analsitas**, permitindo acompanhar **viagens, tratativas, notificações e relatórios de desempenho** em tempo real.  

Com integração direta ao **Firebase Firestore** e a APIs externas (PostgreSQL, Redis e chatbot), o app centraliza informações e automatiza processos operacionais da frota, unindo **tecnologia e logística** em uma interface moderna e responsiva.

---

## ⚙️ **Tecnologias Utilizadas**

| Camada | Tecnologias |
|--------|--------------|
| 💻 **Linguagem** | Kotlin |
| 🧩 **Arquitetura** | MVVM (Model–View–ViewModel) |
| 🧭 **Jetpack Components** | ViewModel, LiveData, ViewBinding |
| ☁️ **Banco de Dados** | Firebase Firestore, SQL |
| 🗄️ **APIs Externas** | PostgreSQL (via Retrofit) · Redis · ChatBot |
| 📊 **Gráficos** | MPAndroidChart |
| 📄 **Relatórios** | PdfDocument (PDF Generator) |
| 🔔 **Notificações** | Redis API |
| 🎨 **UI Design** | Material Design 3 |

---

## 🧱 **Estrutura do Projeto**

```
main/
├── java/com/example/eitruck/
│   ├── data/
│   │   ├── local/          # Armazenamento local (ex: LoginSave)
│   │   ├── remote/
│   │   │   ├── network/           # Clientes e serviços REST   (Postgres, Redis, Chatbot)
│   │   │   ├── repository/       # Repositórios de acesso a dados
│   │   └── ChatRepository.kt
│   ├── domain/                    # Estratégias e lógica de filtro (Strategy Pattern)
│   ├── model/                     # Modelos de dados
│   ├── ui/                        # Telas, Fragments e ViewModels
│   │   ├── chat_bot/              # Chat com IA (integração Chatbot API)
│   │   ├── dash/                  # Dashboard analítico com gráficos
│   │   ├── filter/                # Componentes de filtro dinâmico
│   │   ├── forgot_password/       # Fluxo de recuperação de senha
│   │   ├── home/                  # Tela principal e listagem
│   │   ├── login/                 # Tela de login e autenticação
│   │   ├── main/                  # Activity principal e ViewModel
│   │   ├── notification/          # Listagem de notificações
│   │   ├── profile/               # Perfil do usuário
│   │   ├── restrict/              # Área restrita (admin)
│   │   ├── settings/              # Configurações
│   │   ├── tratativa/             # Tratativas salvas no Firebase Firestore
│   │   ├── travel/                # Gerenciamento de viagens (pendentes e analisadas)
│   │   └── travel_info/           # Detalhes de viagens
│   ├── utils/                     # Utilitários (PDF, Notificações, Preferências)
│   └── SplashScreen.kt
├── res/                           # Recursos XML (layouts, drawables, menus, valores)
└── AndroidManifest.xml
```

---

## 🧩 **Arquitetura MVVM**

O app é estruturado em **três camadas principais**:

- **Model** → Entidades e repositórios que acessam dados de APIs e Firestore.  
- **ViewModel** → Lógica e gerenciamento de estado das telas.  
- **View (UI)** → Fragments e Activities que exibem os dados e interagem com o usuário.

💡 Essa separação garante **testabilidade, manutenibilidade e escalabilidade**.

---

## 🚀 **Principais Funcionalidades**

| Funcionalidade | Descrição |
|----------------|------------|
| 🔐 **Login** | Autenticação via email/senha e sincronização de sessão local. |
| 🧾 **Tratativas (Firestore)** | Registro e histórico de tratativas individuais por motorista. |
| 📊 **Dashboard Analítico** | Exibição de métricas por região, unidade e segmento. |
| ✈️ **Gestão de Viagens** | Listagem de viagens pendentes e analisadas. |
| 🤖 **ChatBot Integrado** | Chat inteligente com API externa (ChatService + ChatApiClient). |
| 🧾 **Geração de Relatórios** | Criação de relatórios PDF via `PdfReportGenerator` e `PdfReportDashGenerator`. |
| 🔔 **Notificações Dinâmicas** | Sistema de alertas integrado ao Redis. |
| ⚙️ **Área Restrita (Admin)** | Painel de controle com acesso a BIs. |

---

## ☁️ **Integração Firebase**

As **tratativas** são registradas diretamente no **Firestore**, podendo assim consultá-las mesmo após os relatórios serem gerados

---

## 🧠 **Integração com APIs Externas**

A camada `data/remote/network/` conecta o app a múltiplos serviços:
- **PostgresClient.kt** → Dados de viagens, unidades e motoristas.  
- **RedisApiClient.kt** → Gerenciamento de notificações.  
- **ChatApiClient.kt** → Comunicação com chatbot externo.  

Os repositórios da pasta `repository/` abstraem o acesso a cada fonte de dados.

---

## 🎨 **Design e Experiência**

O layout segue os princípios do **Material Design 3**, com:
- Componentes consistentes e adaptativos  
- Ícones vetoriais  
- Layouts responsivos com dimensões específicas

---

## ⚙️ **Como Executar**

### 🧩 Pré-requisitos
- Android Studio Iguana ou superior  
- SDK 34+  
- Conta Firebase configurada  
- Conexão com backend (Postgres e Redis ativos)

### 🚀 Passos
```bash
# Clone o projeto
git clone https://github.com/seuusuario/eitruck-mobile.git

# Abra no Android Studio
cd eitruck-mobile

# Configure o Firebase
# Adicione google-services.json em: app/

# Sincronize e rode
./gradlew assembleDebug
```

---

## 👩‍💻 **Equipe**

| Nome | Função | GitHub / LinkedIn |
|------|--------|------------------|
| **Isabela M. Neu** | DEV | [@isabelamneu](https://github.com/isabelamneu) |
| **Marcelo Paschoareli** | DEV | [@marcelopaschoarelii ](https://github.com/marcelopaschoarelii) |
---

## 📷 **Prévia das Telas**

| Login | Dashboard | Tratativas | ChatBot | Home |
|:------:|:-----------:|:------------:|:----------:|
| <img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/ef3f7196-f80a-4252-ae91-c2b20a5ecf79" />
 <img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/6a6e3b4c-fc3a-4cca-ba5d-6083c632d435" />
 | <img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/3ad5e780-f602-459d-b4e0-c5e8d6836e48" />
 | <img width="735" height="1600" alt="image" src="https://github.com/user-attachments/assets/e0d9c36c-21f3-44db-b111-b39b45eb2e04" />
 | <img width="738" height="1600" alt="image" src="https://github.com/user-attachments/assets/76c3baf3-9f7f-4afe-872e-ed91587f6ea4" />
 |

---
