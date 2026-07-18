# Changelog

## [Unreleased]

- Configuração do Gradle Play Publisher 4.0.0 para publicar no Google Play por linha de comando (faixa padrão `alpha`, envio em AAB)
- Notas de versão da loja versionadas em `app/src/main/play/release-notes/` nos três idiomas
- Chave da conta de serviço do Play mantida fora do controle de versão

- Remoção do código de template não utilizado (Navigation, MainScreen, DataRepository e testes associados) que veio do template do Android Studio
- Remoção das dependências Navigation3 e do plugin kotlin-serialization, agora sem uso
- Guard contra divisão por zero quando "Etanol na Gasolina" = "Etanol no Álcool" (antes exibia "NaN L")
- Migração de `TabRow` (deprecado) para `PrimaryTabRow` (Material 3)

## [1.1] - 2026-07-10 (versionCode 2)

- Internacionalização completa: português, inglês (padrão) e espanhol
- Suporte à seleção de idioma por app (Android 13+, `localeConfig`)
- Moeda formatada pelo locale do dispositivo (antes fixa em R$)
- Entrada numérica aceita vírgula ou ponto como separador decimal
- Mensagens de erro movidas do ViewModel para recursos de string (`CalculatorError`)
- Credenciais de assinatura movidas para `local.properties`/variáveis de ambiente (fora do controle de versão)

## [1.0] (versionCode 1)

- Versão inicial
- Três modos de cálculo: Volume Fixo, Completar tanque e Por Valor
- Percentuais de etanol configuráveis para gasolina e álcool
- Detalhamento em dinheiro na aba Por Valor
