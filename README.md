# Calculadora Etanol E85

Aplicativo Android para calcular a mistura correta de **gasolina + etanol** para atingir uma mistura alvo (ex.: E85), feito em Kotlin com Jetpack Compose.

🔗 **Site / Política de Privacidade:** https://walterfr.github.io/CalculadoraEtanolE85/

## Funcionalidades

O app oferece três modos de cálculo:

| Aba | O que faz |
|---|---|
| **Vol. Fixo** | Calcula quantos litros de gasolina e de etanol abastecer para um volume total desejado |
| **Completar** | Considera o combustível já presente no tanque (volume e % de etanol atual) e calcula o que falta para atingir a mistura alvo ao encher o tanque |
| **Por Valor** | A partir de um valor em dinheiro e dos preços por litro, divide o gasto entre gasolina e etanol mantendo a mistura alvo |

As porcentagens de etanol da gasolina (padrão 27%, E27 brasileiro) e do etanol hidratado (padrão 100%) são configuráveis.

## Idiomas

O app é internacionalizado e acompanha o idioma do dispositivo (incluindo seleção de idioma por app no Android 13+):

- 🇧🇷 Português
- 🇺🇸 Inglês (padrão/fallback)
- 🇪🇸 Espanhol

Moeda e formato numérico (vírgula/ponto decimal) seguem o locale do dispositivo.

## Tecnologias

- Kotlin 2.3 / JVM 17
- Jetpack Compose + Material 3
- MVVM com `ViewModel` + `StateFlow`
- minSdk 24 / targetSdk 36

## Como compilar

```bash
cd android-app
./gradlew assembleDebug
```

### Build de release (assinado)

As credenciais de assinatura **não** ficam no repositório. Crie/edite `android-app/local.properties` (ou defina variáveis de ambiente com os mesmos nomes):

```properties
RELEASE_STORE_PASSWORD=...
RELEASE_KEY_ALIAS=upload
RELEASE_KEY_PASSWORD=...
```

E coloque o arquivo `release.keystore` em `android-app/app/` (também ignorado pelo git). Depois:

```bash
./gradlew assembleRelease   # APK
./gradlew bundleRelease     # AAB (formato exigido pelo Google Play)
```

## Publicação no Google Play

O projeto usa o [Gradle Play Publisher](https://github.com/Triple-T/gradle-play-publisher) (GPP 4.x, necessário para AGP 9) para enviar versões pela linha de comando.

### Configuração inicial (uma vez)

1. No **Google Cloud Console**, na conta ligada ao Play: ative a *Google Play Android Developer API*, crie uma **conta de serviço** e gere uma chave **JSON**.
2. No **Play Console** → *Usuários e permissões*: convide o e-mail dessa conta de serviço e conceda as permissões de release.
3. Salve o JSON em `android-app/app/play-service-account.json` (já ignorado pelo git). Para usar outro caminho, defina `PLAY_SERVICE_ACCOUNT_JSON` no `local.properties` ou como variável de ambiente.

> ⚠️ O JSON dá direito de publicar em seu nome. Trate como o keystore: nunca no repositório; em CI, use um *secret*.

### Enviando uma versão

```bash
./gradlew publishReleaseBundle    # envia o AAB para a faixa configurada
./gradlew publishReleaseListing   # envia apenas os metadados da ficha
```

A faixa padrão é **`alpha`** (teste fechado), definida no bloco `play {}` do [app/build.gradle.kts](android-app/app/build.gradle.kts). Para enviar a outra faixa sem alterar o arquivo:

```bash
./gradlew publishReleaseBundle --track internal
```

### Notas de versão

Ficam em `app/src/main/play/release-notes/<locale>/default.txt`, com um arquivo por idioma (`pt-BR`, `en-US`, `es-ES`), acompanhando os idiomas do app. Limite de 500 caracteres por idioma.

> **Acesso de produção:** contas pessoais precisam de um teste fechado com no mínimo **12 testadores por 14 dias contínuos** antes de solicitar a produção. Até lá, apenas as faixas de teste aceitam envios.

## Estrutura do repositório

```
├── android-app/        # Projeto Android (Gradle)
├── index.html          # Página do GitHub Pages
├── privacidade.html    # Política de privacidade (Google Play)
└── CHANGELOG.md        # Histórico de versões
```

## Versões

Veja o [CHANGELOG.md](CHANGELOG.md). Versão atual: **1.1** (versionCode 2).
