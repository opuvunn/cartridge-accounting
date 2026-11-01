# Демонстрационное заполнение склада (не реальные остатки)
# Источник моделей: Сводный_перечень_принтеров_и_картриджей.xlsx
# Запуск из папки проекта, после запуска Spring Boot на порту 8080.
param(
    [string]$BaseUrl = 'http://localhost:8080'
)

$ErrorActionPreference = 'Stop'
$BaseUrl = $BaseUrl.TrimEnd('/')
$projectDir = $PSScriptRoot

# Количества условные. В данных нет ФИО и названия учреждения.
$stockJson = @'
[
  {
    "cartridgeModel": "057 Cartridge",
    "operationType": "RECEIPT",
    "quantity": 7,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "C-006R01573",
    "operationType": "RECEIPT",
    "quantity": 4,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-106R01159",
    "operationType": "RECEIPT",
    "quantity": 9,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-106R01277",
    "operationType": "RECEIPT",
    "quantity": 6,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-106R02181",
    "operationType": "RECEIPT",
    "quantity": 3,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-106R02183",
    "operationType": "RECEIPT",
    "quantity": 5,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "C-108R00909",
    "operationType": "RECEIPT",
    "quantity": 2,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-CE278A; C-728",
    "operationType": "RECEIPT",
    "quantity": 8,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-EXV42",
    "operationType": "RECEIPT",
    "quantity": 5,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-MLT-D104S",
    "operationType": "RECEIPT",
    "quantity": 11,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-MLT-D108S",
    "operationType": "RECEIPT",
    "quantity": 6,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "C-Q2612A/FX-10;703",
    "operationType": "RECEIPT",
    "quantity": 4,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-Q6000A",
    "operationType": "RECEIPT",
    "quantity": 3,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-Q6001A",
    "operationType": "RECEIPT",
    "quantity": 7,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-Q6002A",
    "operationType": "RECEIPT",
    "quantity": 5,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-Q6003A",
    "operationType": "RECEIPT",
    "quantity": 2,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "C-T-1640E",
    "operationType": "RECEIPT",
    "quantity": 6,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-TK-1120",
    "operationType": "RECEIPT",
    "quantity": 9,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-TK-1140",
    "operationType": "RECEIPT",
    "quantity": 12,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-TK-1150",
    "operationType": "RECEIPT",
    "quantity": 5,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-TK-1160",
    "operationType": "RECEIPT",
    "quantity": 4,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "C-TK-435",
    "operationType": "RECEIPT",
    "quantity": 3,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-TN-2335",
    "operationType": "RECEIPT",
    "quantity": 10,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "C-TN-3430",
    "operationType": "RECEIPT",
    "quantity": 7,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "HP Ink Advantage 652",
    "operationType": "RECEIPT",
    "quantity": 4,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "ITSF-CLI451XLC",
    "operationType": "RECEIPT",
    "quantity": 3,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "ITSF-CLI451XLM",
    "operationType": "RECEIPT",
    "quantity": 2,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "ITSF-CLI451XLY",
    "operationType": "RECEIPT",
    "quantity": 6,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "ITSF-PGI450XLBK",
    "operationType": "RECEIPT",
    "quantity": 3,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "KX-FAT411A7",
    "operationType": "RECEIPT",
    "quantity": 4,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "KX-FAT92A",
    "operationType": "RECEIPT",
    "quantity": 8,
    "type": "CARTRIDGE",
    "minStock": 3
  },
  {
    "cartridgeModel": "SP 110E",
    "operationType": "RECEIPT",
    "quantity": 5,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "TK-1140",
    "operationType": "RECEIPT",
    "quantity": 7,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "TK-1200",
    "operationType": "RECEIPT",
    "quantity": 2,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "TN-3430",
    "operationType": "RECEIPT",
    "quantity": 6,
    "type": "CARTRIDGE",
    "minStock": 2
  },
  {
    "cartridgeModel": "DR-2335",
    "operationType": "RECEIPT",
    "quantity": 3,
    "type": "DRUM",
    "minStock": 1
  },
  {
    "cartridgeModel": "DR-3400",
    "operationType": "RECEIPT",
    "quantity": 2,
    "type": "DRUM",
    "minStock": 1
  }
]
'@

# Модели принтеров - СПРАВОЧНИК, не отдельная таблица БД.
# Некоторые обозначения исходного документа требуют проверки по шильдику.
$printerJson = @'
[
  "Brother HL-L2300DR",
  "Brother MFC-L2700D",
  "Brother MFC-L2700DWR",
  "Brother MFC-L5700DR",
  "Brother MFC-L5750DW",
  "Brother MFCL5700DNYJ1",
  "Canon imageRUNNER 2202",
  "Canon LBP2900",
  "Canon LBP2900B",
  "Canon LBP6000",
  "Canon MF443dw",
  "Canon MF4550d",
  "Epson L312",
  "HP 1010",
  "HP 1018",
  "HP 1020",
  "HP 1320",
  "HP Color LaserJet 1600",
  "HP M1212df",
  "HP M1212nf MFP",
  "HP MFP M438",
  "HP OfficeJet Pro 8710",
  "HP P1010",
  "HP P1102",
  "HP P11025",
  "HP P1102s",
  "HP P1103",
  "Kyocera FS-1035",
  "Kyocera FS-1035MFP",
  "Kyocera FS-1060DN",
  "Kyocera FS-1125MFP",
  "Kyocera M2035dn",
  "Kyocera M2235dn",
  "Kyocera M2735dn",
  "Kyocera P2040dn",
  "Kyocera TASKalfa 180",
  "Panasonic KX-MB2030",
  "Panasonic KX-MB2051",
  "Pantum M6500",
  "Pantum P2200",
  "Pantum P6500",
  "Samsung ML-1865w",
  "Samsung ML-2240",
  "Samsung Xpress M2020",
  "Xerox Phaser 3010",
  "Xerox Phaser 3140",
  "Xerox WorkCentre 5020",
  "Xerox WorkCentre 5021"
]
'@

$materials = @($stockJson | ConvertFrom-Json)
$printers = @($printerJson | ConvertFrom-Json)

# Исправление вложенных массивов PowerShell
while ($materials.Count -eq 1 -and $materials[0] -is [array]) {
    $materials = $materials[0]
}

while ($printers.Count -eq 1 -and $printers[0] -is [array]) {
    $printers = $printers[0]
}

Write-Host "ПРОВЕРКА: материалов $($materials.Count), принтеров $($printers.Count)"

if ($materials.Count -ne 37 -or $printers.Count -ne 48) {
    throw "Ошибка структуры данных. Загрузка отменена."
}

Write-Host 'Проверяем доступность Spring Boot...' -ForegroundColor Cyan
try {
    $state = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/state" -TimeoutSec 15
} catch {
    throw "Сервис не отвечает по адресу $BaseUrl/api/state. Запустите .\gradlew.bat bootRun в отдельном терминале."
}
if ($null -eq $state.PSObject.Properties['cartridges']) {
    throw 'Ответ сервиса не содержит список cartridges. Загрузка отменена.'
}

# Создаём справочник для будущих подсказок в форме замены.
$catalogFile = Join-Path $projectDir 'src\main\resources\static\printer-models.json'
$catalogDir = Split-Path -Parent $catalogFile
if (-not (Test-Path -LiteralPath $catalogDir)) {
    throw "Не найдена папка: $catalogDir. Сохраните скрипт в корне cartridge-accounting."
}
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText($catalogFile, $printerJson + [Environment]::NewLine, $utf8NoBom)
Write-Host ("Создан справочник принтеров: {0} моделей" -f $printers.Count) -ForegroundColor Cyan

# Не дублируем модели, уже имеющиеся в базе.
$existing = @{}
foreach ($row in @($state.cartridges)) {
    if ($null -ne $row -and -not [string]::IsNullOrWhiteSpace($row.model)) {
        $existing[$row.model.Trim().ToUpperInvariant()] = $true
    }
}

$created = 0
$skipped = 0
foreach ($item in $materials) {
    $model = $item.cartridgeModel.Trim()
    $key = $model.ToUpperInvariant()
    if ($existing.ContainsKey($key)) {
        Write-Host ("ПРОПУЩЕНО (уже есть): {0}" -f $model) -ForegroundColor DarkYellow
        $skipped++
        continue
    }

    # Запрос к нашему существующему API. История поступления формируется приложением.
    $body = $item | ConvertTo-Json -Compress
    try {
        $response = Invoke-RestMethod -Method Post `
            -Uri "$BaseUrl/api/operations" `
            -ContentType 'application/json; charset=utf-8' `
            -Body $body -TimeoutSec 20
    } catch {
        throw ("Ошибка добавления {0}: {1}. Ранее добавленные позиции сохранятся; повторный запуск пропустит их." -f $model, $_.Exception.Message)
    }
    if ($response.model -ne $model -or [int]$response.balance -ne [int]$item.quantity) {
        throw ("Неожиданный ответ сервиса для {0}. Проверьте склад вручную." -f $model)
    }
    $existing[$key] = $true
    $created++
    Write-Host ("ДОБАВЛЕНО: {0} ({1} шт.)" -f $model, $item.quantity) -ForegroundColor Green
}

$finalState = Invoke-RestMethod -Method Get -Uri "$BaseUrl/api/state" -TimeoutSec 15
Write-Host ''
Write-Host ("Готово. Добавлено: {0}; пропущено: {1}; всего моделей на складе: {2}." -f `
    $created, $skipped, @($finalState.cartridges).Count) -ForegroundColor Green
Write-Host 'Остатки в скрипте ДЕМОНСТРАЦИОННЫЕ, а не фактические.'
Write-Host 'Для отображения перечня принтеров в форме потребуется подключить printer-models.json в index.html.'
