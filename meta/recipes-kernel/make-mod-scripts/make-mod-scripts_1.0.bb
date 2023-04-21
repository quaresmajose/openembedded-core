SUMMARY = "Build tools needed by external modules"
HOMEPAGE = "https://www.yoctoproject.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit kernel-arch linux-kernel-base
inherit pkgconfig

PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

# zlib is required when module signing is enabled
do_configure[depends] += "virtual/kernel:do_shared_workdir openssl-native:do_populate_sysroot zlib-native:do_populate_sysroot"
do_compile[depends] += "virtual/kernel:do_compile_kernelmodules"

DEV_PKG_DEPENDENCY = ""

DEPENDS += "bc-native bison-native"
DEPENDS += "gmp-native"
# required for module signing support
DEPENDS += "elfutils-native"

# we are statically building the support tools, since the output of the build is
# stored in STAGING_KERNEL_BUILDDIR. We do not want any dynamic references to
# libraries that are only present in the recipe native sysroot
EXTRA_OEMAKE = " HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS} -static" HOSTCPP="${BUILD_CPP}""
EXTRA_OEMAKE += " HOSTCXX="${BUILD_CXX} ${BUILD_CXXFLAGS} ${BUILD_LDFLAGS} -static" CROSS_COMPILE=${TARGET_PREFIX}"

# Build some host tools under work-shared.  CC, LD, and AR are probably
# not used, but this is the historical way of invoking "make scripts".
#
do_configure() {
	# setup native pkg-config variables, HOSTPKG_CONFIG is available in newer kernels
	# but we keep these to support older kernels that may not have the variable to
	# abstract calls to pkg-config
	export PKG_CONFIG_DIR="${STAGING_DIR_NATIVE}${libdir_native}/pkgconfig"
	export PKG_CONFIG_PATH="$PKG_CONFIG_DIR:${STAGING_DATADIR_NATIVE}/pkgconfig"
	export PKG_CONFIG_LIBDIR="$PKG_CONFIG_DIR"
	export PKG_CONFIG_SYSROOT_DIR=""

	# for pre-5.15 kernels
	LIBELF_LIBS=$(pkg-config libelf --libs 2>/dev/null || echo -lelf)
	export LIBELF_LIBS="$HOST_LIBELF_LIBS -lz"
	export HOSTLDFLAGS="-lz"

	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	for t in prepare scripts_basic scripts; do
		oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		AR="${KERNEL_AR}" OBJCOPY="${KERNEL_OBJCOPY}" \
		STRIP="${KERNEL_STRIP}" \
		HOSTPKG_CONFIG="pkg-config --static" \
		-C ${STAGING_KERNEL_DIR} O=${STAGING_KERNEL_BUILDDIR} $t
	done
}
