param(
    [Int]$blinkRate=500
)

set-ItemProperty -Path ‘HKCU:\Control Panel\Desktop’ -Name “CursorBlinkRate” Value $blinkRate