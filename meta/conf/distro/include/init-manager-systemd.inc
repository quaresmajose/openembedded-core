# Use systemd for system initialization
DISTRO_FEATURES:append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED:append = " sysvinit"
VIRTUAL-RUNTIME_init_manager ??= "systemd"
VIRTUAL-RUNTIME_initscripts ??= "systemd-compat-units"
VIRTUAL-RUNTIME_login_manager ??= "shadow-base"
VIRTUAL-RUNTIME_dev_manager ??= "systemd"

# Use modules-load and modprobe config path distribution specific
modulesloaddir ?= "${libdir}/modules-load.d"
modprobedir ?= "${nonarch_base_libdir}/modprobe.d"
