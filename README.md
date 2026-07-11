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
./gradlew assembleRelease
```

## Estrutura do repositório

```
├── android-app/        # Projeto Android (Gradle)
├── index.html          # Página do GitHub Pages
├── privacidade.html    # Política de privacidade (Google Play)
└── CHANGELOG.md        # Histórico de versões
```

## Versões

Veja o [CHANGELOG.md](CHANGELOG.md). Versão atual: **1.1** (versionCode 2).
