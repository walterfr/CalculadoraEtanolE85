# Changelog

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
