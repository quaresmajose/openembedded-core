KBRANCH ?= "v6.1/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc
include recipes-kernel/linux/cve-exclusion_6.1.inc

# board specific branches
KBRANCH:qemuarm  ?= "v6.1/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v6.1/standard/qemuarm64"
KBRANCH:qemumips ?= "v6.1/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v6.1/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v6.1/standard/base"
KBRANCH:qemuriscv32  ?= "v6.1/standard/base"
KBRANCH:qemux86  ?= "v6.1/standard/base"
KBRANCH:qemux86-64 ?= "v6.1/standard/base"
KBRANCH:qemuloongarch64  ?= "v6.1/standard/base"
KBRANCH:qemumips64 ?= "v6.1/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "4b6d6ef48b0d11dfdcdc9ae25e1c83be9396a116"
SRCREV_machine:qemuarm64 ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemuloongarch64 ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemumips ?= "8a0e36da16bca1553532936c198c43362be35a82"
SRCREV_machine:qemuppc ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemuriscv64 ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemuriscv32 ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemux86 ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemux86-64 ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_machine:qemumips64 ?= "3407157586b654c9932356124429ee9dc9f56f18"
SRCREV_machine ?= "d08880e7ec70e70249f80b8305da8e90bd47c606"
SRCREV_meta ?= "74fa91143e9076e0d1d5ff0cca93987b3330bf27"

# set your preferred provider of linux-yocto to 'linux-yocto-upstream', and you'll
# get the <version>/base branch, which is pure upstream -stable, and the same
# meta SRCREV as the linux-yocto-standard builds. Select your version using the
# normal PREFERRED_VERSION settings.
BBCLASSEXTEND = "devupstream:target"
SRCREV_machine:class-devupstream ?= "7c58bfa711cb556ef1edc48e7dfa6d84e5fb8912"
PN:class-devupstream = "linux-yocto-upstream"
KBRANCH:class-devupstream = "v6.1/base"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH};protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.1;destsuffix=${KMETA};protocol=https"
SRC_URI += "file://0001-perf-cpumap-Make-counter-as-unsigned-ints.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "6.1.72"

PV = "${LINUX_VERSION}+git"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "^(qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32|qemuloongarch64)$"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc cfg/net/mdio.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
KERNEL_FEATURES:append:powerpc =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64 =" arch/powerpc/powerpc-debug.scc"
KERNEL_FEATURES:append:powerpc64le =" arch/powerpc/powerpc-debug.scc"

INSANE_SKIP:kernel-vmlinux:qemuppc64 = "textrel"

