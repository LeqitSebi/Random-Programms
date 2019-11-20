param(
    [String]$folder=(pwd).Path,
    [Integer]$schwellwert=10485760
)

$disabled = false

while ($true) {
    $current = Get-ChildItem $folder | Measure-Object length -sum | Select-Object -ExpandProperty sum
    if (($current -gt $schwellwert) -and ($disabled==false)) {
        Disable-NetAdapter -Name "WLAN" -Confirm:$false
        Write-Host -ForegroundColor Red "NIC deactivated!"
        $disabled = true
    }
    if (($current -lt $schwellwert) -and ($disabled==true)) {
        Enable-NetAdapter -Name "WLAN" -Confirm:$false
        Write-Host -ForegroundColor Green "NIC activated!"
        $disabled = false
    }
    Start-Sleep 1
}