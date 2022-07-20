SUMMARY = "Build tools needed by external modules"
HOMEPAGE = "https://www.yoctoproject.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit kernel-arch pkgconfig nopackages

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_compile[depends] += "virtual/kernel:do_shared_workdir"

RDEPENDS:${PN}-dev = ""

DEPENDS += " \
	bc-native \
	bison-native \
	gmp-native \
	openssl-native \
	"

EXTRA_OEMAKE = " \
	HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" \
	HOSTCPP="${BUILD_CPP}" \
	HOSTCXX="${BUILD_CXX} ${BUILD_CXXFLAGS} ${BUILD_LDFLAGS}" \
	CROSS_COMPILE="${TARGET_PREFIX}" \
	"

# There's nothing to do here, except compile the scripts
deltask do_fetch
deltask do_unpack
deltask do_patch
deltask do_configure
deltask do_install
deltask do_populate_sysroot

# Build some host tools under work-shared.  CC, LD, and AR are probably
# not used, but this is the historical way of invoking "make scripts".
#
do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	for t in prepare scripts_basic scripts; do
		oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
		-C ${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} $t
	done
}
