package pt.iscte.daam.appvirtual;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.Serializable;

import pt.iscte.daam.appvirtual.adapter.ViewPagerAdapter;
import pt.iscte.daam.appvirtual.entity.ProdutoNotification;
import pt.iscte.daam.appvirtual.fragment.FragmentCompras;
import pt.iscte.daam.appvirtual.fragment.FragmentPerfil;
import pt.iscte.daam.appvirtual.fragment.FragmentProdutos;
import pt.iscte.daam.appvirtual.gcm.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Drawer drawer;

    private BroadcastReceiver registrationBroadcastReceiver;

    private static final long ID_ND_FOOTER = 500;
    private static final long ID_ND_PRODUTOS = 501;

    private static final String REGISTRATION_COMPPLETE = "REGISTRATION_COMPPLETE";
    private static final String PUSH = "PUSH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        configurarViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final PrimaryDrawerItem itemPefil = new PrimaryDrawerItem()
                .withName("Perfil")
                .withIcon(GoogleMaterial.Icon.gmd_person);
        final PrimaryDrawerItem itemProdutos = new PrimaryDrawerItem()
                .withName("Produtos")
                .withBadge("43")
                .withIdentifier(ID_ND_PRODUTOS)
                .withIcon(FontAwesome.Icon.faw_th_list)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));
        final PrimaryDrawerItem itemCompras = new PrimaryDrawerItem()
                .withName("Últimas Compras")
                .withBadge("2")
                .withIcon(GoogleMaterial.Icon.gmd_shop_two)
                .withBadgeStyle(new BadgeStyle()
                        .withTextColor(Color.WHITE)
                        .withColorRes(R.color.md_orange_700));

        AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Ansumane Mané")
                                .withEmail("ameen@iscte-iul.pt")
                                .withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(drawerHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Conta do Usuário"),
                        itemPefil,
                        new SectionDrawerItem().withName("Ações do Sistema"),
                        itemProdutos,
                        itemCompras
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        configuraItensDrawer(position, drawerItem);
                        return true;
                    }
                })
                .build();

        drawer.addStickyFooterItem(new PrimaryDrawerItem()
                .withName("Sobre o App")
                .withIcon(GoogleMaterial.Icon.gmd_info)
                .withIdentifier(ID_ND_FOOTER));

        Serializable serializable = getIntent().getExtras() != null ? getIntent().getExtras().getSerializable("nf_produto") : null;
        if (serializable != null) {
            ProdutoNotification nf_produto = (ProdutoNotification) serializable;
            Toast.makeText(MainActivity.this, nf_produto.toString(), Toast.LENGTH_SHORT).show();
        }

        configurarGCM();
    }

    private void configuraItensDrawer(int position, IDrawerItem drawerItem) {
        viewPager.setCurrentItem(position);

        switch ((int) drawerItem.getIdentifier()) {
            case (int) ID_ND_FOOTER:
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);

                    String versao = info.versionName;
                    Toast.makeText(MainActivity.this, "Versão: " + versao, Toast.LENGTH_SHORT).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case (int) ID_ND_PRODUTOS:
                Intent intent = new Intent(this, ListaProdutosActivity.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer();
    }

    private void configurarViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentCompras(), "Compras");
        viewPagerAdapter.addFragment(new FragmentProdutos(), "Produtos");
        viewPagerAdapter.addFragment(new FragmentPerfil(), "Perfil");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configurarGCM() {
        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getStringExtra("token");
                Toast.makeText(MainActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
            }
        };

        Intent intent = new Intent(this, RegistrationIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver, new IntentFilter(REGISTRATION_COMPPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver, new IntentFilter(PUSH));
    }
}
